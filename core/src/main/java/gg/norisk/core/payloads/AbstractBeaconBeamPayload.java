package gg.norisk.core.payloads;

/**
 * Abstract payload for creating beacon beams at specific coordinates
 */
public abstract class AbstractBeaconBeamPayload extends AbstractPayload {
    private final XYZ xyz;
    private final Dimension dimension;
    private final RGBColor color;
    
    /**
     * Creates a new beacon beam payload
     * @param xyz The coordinates where the beacon beam should appear
     * @param dimension The dimension where the beacon beam should appear
     * @param color The RGB color of the beacon beam
     */
    public AbstractBeaconBeamPayload(XYZ xyz, Dimension dimension, RGBColor color) {
        super("beacon_beam");
        this.xyz = xyz;
        this.dimension = dimension;
        this.color = color;
    }
    
    public XYZ getXyz() {
        return xyz;
    }
    
    public Dimension getDimension() {
        return dimension;
    }
    
    public RGBColor getColor() {
        return color;
    }
} 