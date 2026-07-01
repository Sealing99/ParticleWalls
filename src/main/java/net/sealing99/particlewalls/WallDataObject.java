package net.sealing99.particlewalls;

public class WallDataObject {
    public String id;
    public int x, y, z;
    public String xz;
    public int width;
    public int height;
    public int r, g, b;

    public WallDataObject(String id, int x, int y, int z,
                          String xz, int width, int height,
                          int r, int g, int b) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xz = xz;
        this.width = width;
        this.height = height;
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
