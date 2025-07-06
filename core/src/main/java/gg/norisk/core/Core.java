package gg.norisk.core;

import java.util.List;
import java.util.UUID;

import gg.norisk.core.payloads.AbstractBeaconBeamPayload;
import gg.norisk.core.payloads.AbstractGamemodePayload;
import gg.norisk.core.payloads.AbstractInputbarPayload;
import gg.norisk.core.payloads.AbstractModuleDeactivatePayload;
import gg.norisk.core.payloads.AbstractToastPayload;
import gg.norisk.core.payloads.AbstractWheelPayload;
import gg.norisk.core.payloads.Dimension;
import gg.norisk.core.payloads.Modules;
import gg.norisk.core.payloads.RGBColor;
import gg.norisk.core.payloads.ToastType;
import gg.norisk.core.payloads.XYZ;

public class Core {
    
    public static void main(String[] args) {
        System.out.println("Initializing NoRiskClient-Server-API Core");
        throw new RuntimeException("Failed to initialize NoRiskClient-Server-API Core");
    }
    
    /**
     * Creates a toast notification payload to display messages in the NoRisk Client
     *
     * @param progressBar Whether to show a progress bar in the toast
     * @param header The header text of the toast notification
     * @param content The main content text of the toast notification
     * @param playerHead Whether to show the player's avatar in the toast
     * @param playerUUID The UUID of the player whose avatar to show (required if playerHead is true)
     * @param toastType The type of toast (INFO, SUCCESS, ERROR) which determines the color scheme
     * @return AbstractToastPayload The created toast payload
     */
    public static AbstractToastPayload createToastPayload(
            boolean progressBar,
            String header,
            String content,
            boolean playerHead,
            UUID playerUUID,
            ToastType toastType
    ) {
        return new AbstractToastPayload(progressBar, header, content, playerHead, playerUUID, toastType) {};
    }
    
    /**
     * Creates a module deactivate payload to disable specific NoRisk Client modules
     *
     * @param modules List of modules to deactivate on the client
     * @return AbstractModuleDeactivatePayload The created module deactivate payload
     */
    public static AbstractModuleDeactivatePayload createModuleDeactivatePayload(
            List<Modules> modules
    ) {
        return new AbstractModuleDeactivatePayload(modules) {};
    }
    
    /**
     * Creates a beacon beam payload to display colored light beams at specific coordinates
     *
     * @param xyz The X, Y, Z coordinates where the beacon beam should appear
     * @param dimension The dimension where the beacon beam should appear (OVERWORLD, NETHER, END)
     * @param color The RGB color of the beacon beam
     * @return AbstractBeaconBeamPayload The created beacon beam payload
     */
    public static AbstractBeaconBeamPayload createBeaconBeamPayload(
            XYZ xyz,
            Dimension dimension,
            RGBColor color
    ) {
        return new AbstractBeaconBeamPayload(xyz, dimension, color) {};
    }
    
    /**
     * Creates a gamemode payload to override the gamemode display in the NoRisk Client
     *
     * @param gamemode The custom gamemode name to display
     * @return AbstractGamemodePayload The created gamemode payload
     */
    public static AbstractGamemodePayload createGamemodePayload(
            String gamemode
    ) {
        return new AbstractGamemodePayload(gamemode) {};
    }
    
    /**
     * Creates a wheel payload to add entries to the NoRisk Client action wheel
     *
     * @param name The display name of the action wheel entry
     * @param command The command to execute when the entry is selected
     * @return AbstractWheelPayload The created wheel payload
     */
    public static AbstractWheelPayload createWheelPayload(
            String name,
            String command
    ) {
        return new AbstractWheelPayload(name, command) {};
    }
    
    /**
     * Creates an inputbar payload to show input prompts in the NoRisk Client
     *
     * @param input The header text for the input prompt
     * @param placeholder The placeholder text shown in the input field
     * @param maxLength The maximum number of characters allowed in the input
     * @return AbstractInputbarPayload The created inputbar payload
     */
    public static AbstractInputbarPayload createInputbarPayload(
            String input,
            String placeholder,
            int maxLength
    ) {
        return new AbstractInputbarPayload(input, placeholder, maxLength) {};
    }
} 