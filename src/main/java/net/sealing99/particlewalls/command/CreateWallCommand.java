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

public class CreateWallCommand implements CommandExecutor, TabCompleter {
    public CreateWallCommand(ParticleWalls plugin) {}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command cannot be executed on non-players.");
            return true;
        }

        if (args.length != 10) {
            sender.sendMessage("Incorrect usage: /createwall <id> <x> <y> <z> <x|z> <width> <height> <r> <g> <b>");
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

        if (!(xz.equals("x") | xz.equals("z"))) {
            sender.sendMessage("Incorrect usage: /createwall <id> <x> <y> <z> <x|z> <width> <height> <r> <g> <b>");
            return true;
        }

        if (r > 256 | g > 256 | b > 256) {
            sender.sendMessage("Color RGB cannot be higher than 256.");
            return true;
        }

        List<WallDataObject> walls = ParticleWalls.getWalls();
        WallDataObject wall = new WallDataObject(id, x, y, z, xz, width, height, r, g, b);
        walls.add(wall);

        ParticleWalls.overwriteWalls(walls);

        sender.sendMessage("Successfully created wall " + id);

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
