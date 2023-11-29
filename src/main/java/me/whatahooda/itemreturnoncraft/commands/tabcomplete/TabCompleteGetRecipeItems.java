package me.whatahooda.itemreturnoncraft.commands.tabcomplete;

import me.whatahooda.itemreturnoncraft.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class TabCompleteGetRecipeItems implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender _sender, Command _command, String _label, String[] _args) {
        if (_args.length == 1) return ConfigManager.SUB_COMMANDS_RECIPE_TYPE;

        String type = _args[0];
        if (_args.length == 2) {
            if (type.equals("general")) return ConfigManager.getManager().getRecipeNamesGeneral();
            if (type.equals("nbt")) return ConfigManager.getManager().getRecipeNamesNBT();
        }

        return null;
    }
}
