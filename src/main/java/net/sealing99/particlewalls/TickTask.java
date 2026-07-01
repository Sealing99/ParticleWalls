package net.sealing99.particlewalls;

import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TickTask extends BukkitRunnable {
    private static final World world = ParticleWalls.defaultWorld;

    public TickTask() {
        ParticleWalls.getInstance().getLogger().info("Started ticker");
    }

    @Override
    public void run() {
        if (world == null) return;

        List<WallDataObject> walls = ParticleWalls.getWalls();

        for (WallDataObject wall : walls) {
            for (Location location : getPositions(wall.x, wall.y, wall.z, wall.xz, wall.width, wall.height)) {
                world.spawnParticle(
                        Particle.DUST, location, 1,
                        0, 0, 0,
                        new Particle.DustOptions(
                                Color.fromRGB(wall.r, wall.g, wall.b)
                                , 1.0f)
                );
           }
        }
    }

    private static List<Location> getPositions(int x, int y, int z, String xz, int width, int height) {
        List<Location> list = new ArrayList<>();
        boolean alongX = (xz.equals("x"));

        if (alongX) x -= (width-1)/2;
        if (!alongX) z -= (width-1)/2;

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                if (alongX) {
                    list.add(new Location(world, x + w, y + h, z));
                } else {
                    list.add(new Location(world, x, y + h, z + w));
                }
            }
        }

        return list;
    }
}
