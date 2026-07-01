package net.sealing99.particlewalls.command;

import net.sealing99.particlewalls.ParticleWalls;
import net.sealing99.particlewalls.WallDataObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeleteWallCommand implements CommandExecutor, TabCompleter {
    public DeleteWallCommand(ParticleWalls plugin) {}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command cannot be executed on non-players.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Incorrect usage: /deletewall <id>");
            return true;
        }

        List<WallDataObject> walls = ParticleWalls.getWalls();
        boolean removed = false;

        for (WallDataObject wall : walls) {
            if (wall.id.equals(args[0])) {
                walls.remove(wall);
                break;
            }
        }

        if (!removed) {
            sender.sendMessage("No wall with id " + args[0]);
            return true;
        }

        ParticleWalls.overwriteWalls(walls);
        sender.sendMessage("Successfully removed wall " + args[0]);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            List<WallDataObject> walls = ParticleWalls.getWalls();
            ArrayList<String> ids = new ArrayList<>();

            for (WallDataObject wall : walls) {
                ids.add(wall.id);
            }

            return ids;
        }

        return List.of();
    }
}
