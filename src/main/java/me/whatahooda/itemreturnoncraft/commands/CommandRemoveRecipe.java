package me.whatahooda.itemreturnoncraft.commands;

import me.whatahooda.itemreturnoncraft.ItemReturnOnCraft;
import me.whatahooda.itemreturnoncraft.config.ConfigManager;
import me.whatahooda.itemreturnoncraft.models.ReturnableItemManager;
import me.whatahooda.itemreturnoncraft.util.CraftReturnUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CommandRemoveRecipe implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            ItemReturnOnCraft.getMain().getLogger().log(Level.WARNING, "You must execute removeRecipe as a player entity");
            return false;
        }

        if (!passGuardCases(p, args)) return false;

        String type = args[0];
        String recipeName = args[1];

        ConfigManager.getManager().removeRecipeFromConfig(type, recipeName);
        p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + recipeName + " has been removed");
        ItemReturnOnCraft.getMain().getLogger().log(Level.INFO, p.getName() + " has removed a " + type + " CraftReturn recipe named " + recipeName);
        return true;
    }

    private boolean passGuardCases(Player p, String[] args) {
        if (args.length == 0 || !ConfigManager.SUB_COMMANDS_RECIPE_TYPE.contains(args[0])) {
            p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "You must specify if this is a \"general\" or \"nbt\" recipe");
            return false;
        }
        if (args.length < 2) {
            p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "You must provide a recipe name");
            return false;
        }
        if (args[0].equals("general") && !ReturnableItemManager.getManager().getNamesGeneral().contains(args[1])) {
            p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "You must provide a \"general\" recipe name");
            return false;
        }
        else if (args[0].equals("nbt") && !ReturnableItemManager.getManager().getNamesNBT().contains(args[1])){
            p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "You must provide a \"nbt\" recipe name");
            return false;
        }

        if (args.length < 3 || !args[2].equals("confirm")) {
            p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "Please confirm you want to remove this recipe");
            return false;
        }

        return true;
    }
}
