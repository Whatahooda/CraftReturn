package me.whatahooda.itemreturnoncraft.commands.tabcomplete;

import me.whatahooda.itemreturnoncraft.config.ConfigManager;
import me.whatahooda.itemreturnoncraft.models.ReturnableItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class TabCompleteRemoveRecipe implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender _sender, Command _command, String _label, String[] _args) {
        if (_args.length == 1) return ConfigManager.SUB_COMMANDS_RECIPE_TYPE;

        String type = _args[0];
        if (_args.length == 2) {
            if (type.equals("general")) return ReturnableItemManager.getManager().getNamesGeneral();
            if (type.equals("nbt")) return ReturnableItemManager.getManager().getNamesNBT();
        }

        return null;
    }
}
