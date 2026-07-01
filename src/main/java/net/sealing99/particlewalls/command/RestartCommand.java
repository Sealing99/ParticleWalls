package net.sealing99.particlewalls.command;

import net.sealing99.particlewalls.ParticleWalls;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RestartCommand implements CommandExecutor {
    public RestartCommand(ParticleWalls plugin) {}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        ParticleWalls.getInstance().restartLoop();
        sender.sendMessage("Restarting wall rendering...");
        return true;
    }
}
