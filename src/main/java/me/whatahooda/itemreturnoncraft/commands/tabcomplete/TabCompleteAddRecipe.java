package me.whatahooda.itemreturnoncraft.commands.tabcomplete;

import me.whatahooda.itemreturnoncraft.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class TabCompleteAddRecipe implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return ConfigManager.SUB_COMMANDS_RECIPE_TYPE;
        return null;
    }
}