package gg.norisk.core.payloads;

public interface Payload {
    default String getType() {
        Class<?> clazz = this.getClass();
        gg.norisk.core.annotations.Payload payload = clazz.getAnnotation(gg.norisk.core.annotations.Payload.class);
        if (payload == null) {
            throw new IllegalStateException("Payload class " + clazz.getName() + " is missing @Payload annotation");
        }
        return payload.type();
    }
}
