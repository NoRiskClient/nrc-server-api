package gg.norisk.core.payloads;

/**
 * Data class representing RGB color values
 */
public class RGBColor {
    private final int r;
    private final int g;
    private final int b;
    
    /**
     * Creates a new RGB color
     * @param r The red component (0-255)
     * @param g The green component (0-255)
     * @param b The blue component (0-255)
     */
    public RGBColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public int getR() {
        return r;
    }
    
    public int getG() {
        return g;
    }
    
    public int getB() {
        return b;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RGBColor rgbColor = (RGBColor) obj;
        return r == rgbColor.r && g == rgbColor.g && b == rgbColor.b;
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(r, g, b);
    }
    
    @Override
    public String toString() {
        return "RGBColor{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }
} 