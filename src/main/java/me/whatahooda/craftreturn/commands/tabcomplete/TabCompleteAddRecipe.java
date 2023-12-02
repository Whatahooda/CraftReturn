package me.whatahooda.craftreturn.commands.tabcomplete;

import me.whatahooda.craftreturn.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteAddRecipe implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1) return ConfigManager.SUB_COMMANDS_RECIPE_TYPE;
        return new ArrayList<>();
    }
}