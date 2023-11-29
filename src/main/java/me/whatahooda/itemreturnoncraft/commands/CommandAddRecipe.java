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
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class CommandAddRecipe implements CommandExecutor {

    // Getting the plugin instance for variables and functions
    // I heard dependency injections are better, but I have a hard time wrapping my head around them
    private static final ItemReturnOnCraft PLUGIN_INSTANCE;

    static {
        PLUGIN_INSTANCE = (ItemReturnOnCraft) Bukkit.getServer().getPluginManager().getPlugin("CraftReturn");
    }

    @Override
    public boolean onCommand(CommandSender _sender, Command _command, String _label, String[] _args) {
        if (!(_sender instanceof Player)) {
            PLUGIN_INSTANCE.getLogger().log(Level.WARNING, "You must execute addRecipe as a player entity");
            return false;
        }

        Player p = (Player) _sender;
        String playerName = p.getName();
        ItemStack mainHand = p.getInventory().getItemInMainHand().clone();
        ItemStack offHand = p.getInventory().getItemInOffHand().clone();
        if (!passGuardCases(p, mainHand, offHand, _args)) return false;

        mainHand.setAmount(1);
        offHand.setAmount(1);

        String recipeType = _args[0];
        String returnRecipeName = _args[1];

        ConfigManager.getManager().addRecipeToConfig(recipeType, returnRecipeName, mainHand, offHand, playerName);
        p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + returnRecipeName + " has been added");
        PLUGIN_INSTANCE.getLogger().log(Level.INFO, p.getName() + " has added a " + recipeType + " CraftReturn recipe named " + returnRecipeName);
        PLUGIN_INSTANCE.getLogger().log(Level.INFO, "Craft Item: " + mainHand.getType() + " | Return Item: " + offHand.getType());
        return true;
    }

    private boolean passGuardCases(Player _p, ItemStack _mainHand, ItemStack _offHand, String[] _args) {
        if (_mainHand.getAmount() == 0) {
            _p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "You must hold an item in your main hand to represent the \"craft-item\"");
            return false;
        }
        if (_offHand.getAmount() == 0) {
            _p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "You must hold an item in your off hand to represent the \"return-item\"");
            return false;
        }
        if (_mainHand.getType() == _offHand.getType()) {
            _p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "The \"craft-item\" and \"return-item\" must be different");
            return false;
        }

        if (_args.length < 1) {
            _p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "You must specify if this is a \"general\" or \"nbt\" recipe");
            return false;
        }
        if (!ConfigManager.SUB_COMMANDS_RECIPE_TYPE.contains(_args[0])) {
            _p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "You must specify if this is a \"general\" or \"nbt\" recipe");
            return false;
        }
        if (_args.length < 2) {
            _p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "You must provide a name for this return recipe(no spaces)");
            return false;
        }

        if (_args[0].equals("general") && ConfigManager.getManager().getRecipeNamesGeneral().contains(_args[1])) {
            _p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "A general recipe already has that name");
            return false;
        }
        else if (_args[0].equals("nbt") && ConfigManager.getManager().getRecipeNamesNBT().contains(_args[1])){
            _p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "A nbt recipe already has that name");
            return false;
        }

        if (_args[0].equals("general") && ReturnableItemManager.getManager().isGeneralReturnable(_mainHand)) {
            _p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "There already exists a general recipe with that craft-item named \"" + ReturnableItemManager.getManager().getGeneralName(_mainHand) + "\"");
            return false;
        }
        else if (_args[0].equals("nbt") && ReturnableItemManager.getManager().isNBTReturnable(_mainHand)) {
            _p.sendMessage(CraftReturnUtil.CRAFT_RETURN_TAG + ChatColor.RED + "There already exists a nbt recipe with that craft-item named \"" + ReturnableItemManager.getManager().getNBTName(_mainHand) + "\"");
            return false;
        }

        return true;
    }
}