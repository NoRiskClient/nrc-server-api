package gg.norisk.core.manager;

import gg.norisk.core.common.PacketHandler;
import gg.norisk.core.common.PacketListener;
import gg.norisk.core.payloads.InPayload;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PacketEventManager {

    private final Map<Class<? extends InPayload>, List<ListenerMethod>> listenerMethods = new ConcurrentHashMap<>();

    /**
     * Registriert alle @PacketHandler Methoden in einem Listener-Objekt.
     */
    public void registerListener(PacketListener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PacketHandler.class) && method.getParameterCount() == 2) {
                // Die Methode muss genau zwei Parameter haben: Player und Packet
                Class<?> packetType = method.getParameterTypes()[1];
                if (InPayload.class.isAssignableFrom(packetType)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends InPayload> packetClass = (Class<? extends InPayload>) packetType;
                    ListenerMethod listenerMethod = new ListenerMethod(listener, method);
                    listenerMethods.computeIfAbsent(packetClass, k -> new ArrayList<>()).add(listenerMethod);
                }
            }
        }
    }

    /**
     * Ruft alle relevanten Listener für ein ankommendes Packet auf.
     */
    public void callEvent(UUID sender, InPayload packet) {
        List<ListenerMethod> methods = listenerMethods.get(packet.getClass());
        if (methods != null) {
            for (ListenerMethod listenerMethod : methods) {
                try {
                    listenerMethod.method.invoke(listenerMethod.instance, sender, packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private record ListenerMethod(PacketListener instance, Method method) {}
}