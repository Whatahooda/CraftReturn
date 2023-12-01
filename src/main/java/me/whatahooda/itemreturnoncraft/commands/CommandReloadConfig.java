package me.whatahooda.itemreturnoncraft.commands;

import me.whatahooda.itemreturnoncraft.ItemReturnOnCraft;
import me.whatahooda.itemreturnoncraft.config.ConfigManager;
import me.whatahooda.itemreturnoncraft.util.CraftReturnUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandReloadConfig implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("craft-return.commands.reload-config")) {
            sender.sendMessage(CraftReturnUtil.COMMAND_NO_PERMISSION);
            return true;
        }
        ItemReturnOnCraft.getMain().reloadConfig();
        ConfigManager.getManager().loadConfig();

        return true;
    }
}
