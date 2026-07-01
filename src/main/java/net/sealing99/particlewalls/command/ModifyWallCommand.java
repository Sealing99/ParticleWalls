package net.sealing99.particlewalls.command;

import net.sealing99.particlewalls.ParticleWalls;
import net.sealing99.particlewalls.WallDataObject;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ModifyWallCommand implements CommandExecutor, TabCompleter {
    public ModifyWallCommand(ParticleWalls plugin) {}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command cannot be executed on non-players.");
            return true;
        }

        if (args.length != 10) {
            sender.sendMessage("Incorrect usage: /modifywall <id> <x> <y> <z> <x|z> <width> <height> <r> <g> <b>");
            return true;
        }

        String id = args[0];
        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        int z = Integer.parseInt(args[3]);
        String xz = args[4];
        int width = Integer.parseInt(args[5]);
        int height = Integer.parseInt(args[6]);
        int r = Integer.parseInt(args[7]);
        int g = Integer.parseInt(args[8]);
        int b = Integer.parseInt(args[9]);

        List<WallDataObject> walls = ParticleWalls.getWalls();
        boolean removed = false;

        for (WallDataObject wall : walls) {
            if (wall.id.equals(id)) {
                walls.remove(wall);
                removed = true;
                break;
            }
        }

        if (!removed) {
            sender.sendMessage("No wall with id " + id);
            return true;
        }

        WallDataObject wall = new WallDataObject(id, x, y, z, xz, width, height, r, g, b);

        walls.add(wall);
        ParticleWalls.overwriteWalls(walls);
        sender.sendMessage("Successfully modified wall " + id);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Location location;

        if (sender instanceof Player player) {
            location = player.getLocation();
        } else {
            return List.of();
        }

        if (args.length == 1) {
            List<WallDataObject> walls = ParticleWalls.getWalls();
            ArrayList<String> ids = new ArrayList<>();

            for (WallDataObject wall : walls) {
                ids.add(wall.id);
            }

            return ids;
        }

        if (args.length == 2) {
            String x = Integer.toString(location.getBlockX());
            String y = Integer.toString(location.getBlockY());
            String z = Integer.toString(location.getBlockZ());

            return List.of(x + " " + y + " " + z, x + " " + y, x);
        } else if (args.length == 3) {
            String y = Integer.toString(location.getBlockY());
            String z = Integer.toString(location.getBlockZ());

            return List.of(y + " " + z, y);
        } else if (args.length == 4) {
            String z = Integer.toString(location.getBlockZ());

            return List.of(z);
        }

        if (args.length == 5) {
            return List.of("x", "z");
        }

        return List.of();
    }
}
