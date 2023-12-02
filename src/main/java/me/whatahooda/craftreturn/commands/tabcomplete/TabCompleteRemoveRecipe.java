package me.whatahooda.craftreturn.commands.tabcomplete;

import me.whatahooda.craftreturn.config.ConfigManager;
import me.whatahooda.craftreturn.models.ReturnableItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteRemoveRecipe implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1) return ConfigManager.SUB_COMMANDS_RECIPE_TYPE;

        String type = args[0];
        if (args.length == 2) {
            if (type.equals("general")) return ReturnableItemManager.getManager().getNamesGeneral().stream()
                    .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
            if (type.equals("nbt")) return ReturnableItemManager.getManager().getNamesNBT().stream()
                    .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }

        return new ArrayList<>();
    }
}
