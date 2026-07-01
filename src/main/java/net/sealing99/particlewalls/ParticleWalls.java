package net.sealing99.particlewalls;

import net.sealing99.particlewalls.command.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ParticleWalls extends JavaPlugin {
    public static final String HUMAN_READABLE_PLUGIN_ID = "Particle Walls";

    private static ParticleWalls instance;
    private static TickTask ticker;
    private List<WallDataObject> walls;
    public static World defaultWorld;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("Registering commands for " + HUMAN_READABLE_PLUGIN_ID);

        Objects.requireNonNull(getCommand("createwall")).setExecutor(new CreateWallCommand(this));
        Objects.requireNonNull(getCommand("deletewall")).setExecutor(new DeleteWallCommand(this));
        Objects.requireNonNull(getCommand("modifywall")).setExecutor(new ModifyWallCommand(this));
        Objects.requireNonNull(getCommand("stopwalls")).setExecutor(new StopCommand(this));
        Objects.requireNonNull(getCommand("restartwalls")).setExecutor(new RestartCommand(this));

        getLogger().info("Registering data for " + HUMAN_READABLE_PLUGIN_ID);


        File dataFolder = getDataFolder();

        if (!dataFolder.exists()) {
            getLogger().info("Creating data files for " + HUMAN_READABLE_PLUGIN_ID);
            dataFolder.mkdirs();
        }

        getLogger().info("Loading data for " + HUMAN_READABLE_PLUGIN_ID);

        File dataFile = new File(getDataFolder(), "data.yml");

        walls = loadList(dataFile);

        defaultWorld = Bukkit.getWorld(NamespacedKey.minecraft("overworld"));

        getLogger().info("Done initializing " + HUMAN_READABLE_PLUGIN_ID + "! Running...");
        ticker = new TickTask();
        ticker.runTaskTimer(instance, 0L, 1L);
    }

    @Override
    public void onDisable() {
        File dataFile = new File(getDataFolder(), "data.yml");
        saveList(walls, dataFile);

        getLogger().info("Successfully shut down!");
    }

    public static ParticleWalls getInstance() {
        return instance;
    }

    public static TickTask getTicker() {
        return ticker;
    }

    public static List<WallDataObject> getWalls() {
        return getInstance().walls;
    }

    public void saveList(List<WallDataObject> list, File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("walls", null);

        for (int i = 0; i < list.size(); i++) {
            WallDataObject d = list.get(i);

            String path = "walls." + i;

            config.set(path + ".id", d.id);
            config.set(path + ".x", d.x);
            config.set(path + ".y", d.y);
            config.set(path + ".z", d.z);
            config.set(path + ".xz", d.xz);
            config.set(path + ".width", d.width);
            config.set(path + ".height", d.height);
            config.set(path + ".r", d.r);
            config.set(path + ".g", d.g);
            config.set(path + ".b", d.b);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<WallDataObject> loadList(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        List<WallDataObject> list = new ArrayList<>();

        ConfigurationSection section = config.getConfigurationSection("walls");
        if (section == null) return list;

        for (String key : section.getKeys(false)) {
            String path = "walls." + key;

            WallDataObject d = new WallDataObject(
                    config.getString(path + ".id"),
                    config.getInt(path + ".x"),
                    config.getInt(path + ".y"),
                    config.getInt(path + ".z"),
                    config.getString(path + ".xz"),
                    config.getInt(path + ".width"),
                    config.getInt(path + ".height"),
                    config.getInt(path + ".r"),
                    config.getInt(path + ".g"),
                    config.getInt(path + ".b")
            );

            list.add(d);
        }

        return list;
    }

    public static void overwriteWalls(List<WallDataObject> walls) {
        getInstance().walls = walls;
    }

    public void restartLoop() {
        if (ticker != null) ticker.cancel();
        ticker = new TickTask();
        ticker.runTaskTimer(instance, 0L, 1L);
    }

    public void stopLoop() {
        if (ticker != null) ticker.cancel();
        ticker = null;
    }
}
