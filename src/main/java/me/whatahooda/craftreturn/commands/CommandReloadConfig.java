package me.whatahooda.craftreturn.commands;

import me.whatahooda.craftreturn.CraftReturn;
import me.whatahooda.craftreturn.config.ConfigManager;
import me.whatahooda.craftreturn.util.CraftReturnUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandReloadConfig implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("craftreturn.command.reloadconfig")) {
            sender.sendMessage(CraftReturnUtil.COMMAND_NO_PERMISSION);
            return true;
        }
        CraftReturn.getMain().reloadConfig();
        ConfigManager.getManager().loadConfig();

        return true;
    }
}
