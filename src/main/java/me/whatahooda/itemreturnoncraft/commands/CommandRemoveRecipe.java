package me.whatahooda.itemreturnoncraft.commands;

import me.whatahooda.itemreturnoncraft.ItemReturnOnCraft;
import me.whatahooda.itemreturnoncraft.config.ConfigManager;
import me.whatahooda.itemreturnoncraft.util.CraftReturnUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CommandRemoveRecipe implements CommandExecutor {

    private static final ItemReturnOnCraft PLUGIN_INSTANCE;

    static {
        PLUGIN_INSTANCE = (ItemReturnOnCraft) Bukkit.getServer().getPluginManager().getPlugin("CraftReturn");
    }

    @Override
    public boolean onCommand(CommandSender _sender, Command _command, String _label, String[] _args) {
        if (!(_sender instanceof Player)) {
            PLUGIN_INSTANCE.getLogger().log(Level.WARNING, "You must execute removeRecipe as a player entity");
            return false;
        }

        Player p = (Player) _sender;
        if (!passGuardCases(p, _args)) return false;

        String type = _args[0];
        String recipeName = _args[1];

        ConfigManager.getManager().removeRecipeFromConfig(type, recipeName);
        p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + recipeName + " has been removed");
        PLUGIN_INSTANCE.getLogger().log(Level.INFO, p.getName() + " has removed a " + type + " CraftReturn recipe named " + recipeName);
        return true;
    }

    private boolean passGuardCases(Player _p, String[] _args) {
        if (_args.length == 0 || !ConfigManager.SUB_COMMANDS_RECIPE_TYPE.contains(_args[0])) {
            _p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "You must specify if this is a \"general\" or \"nbt\" recipe");
            return false;
        }
        if (_args.length < 2) {
            _p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "You must provide a recipe name");
            return false;
        }
        if (_args[0].equals("general") && !ConfigManager.getManager().getRecipeNamesGeneral().contains(_args[1])) {
            _p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "You must provide a \"general\" recipe name");
            return false;
        }
        else if (_args[0].equals("nbt") && !ConfigManager.getManager().getRecipeNamesNBT().contains(_args[1])){
            _p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "You must provide a \"nbt\" recipe name");
            return false;
        }

        if (_args.length < 3 || !_args[2].equals("confirm")) {
            _p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "Please confirm you want to remove this recipe");
            return false;
        }

        return true;
    }
}
