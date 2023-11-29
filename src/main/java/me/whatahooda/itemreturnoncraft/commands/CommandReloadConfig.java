package me.whatahooda.itemreturnoncraft.commands;

import me.whatahooda.itemreturnoncraft.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandReloadConfig implements CommandExecutor {
    // TODO Find out how to read manually entered and saved data to the config
    @Override
    public boolean onCommand(CommandSender _sender, Command _command, String _label, String[] _args) {
        ConfigManager.getManager().loadConfig();

        return true;
    }
}
