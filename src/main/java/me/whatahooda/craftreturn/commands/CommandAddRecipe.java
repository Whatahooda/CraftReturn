package me.whatahooda.craftreturn.commands;

import me.whatahooda.craftreturn.CraftReturn;
import me.whatahooda.craftreturn.config.ConfigManager;
import me.whatahooda.craftreturn.models.ReturnableItemManager;
import me.whatahooda.craftreturn.util.CraftReturnUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class CommandAddRecipe implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("craftreturn.command.addrecipe")) {
            sender.sendMessage(CraftReturnUtil.COMMAND_NO_PERMISSION);
            return true;
        }
        if (!(sender instanceof Player p)) {
            sender.sendMessage("You must execute addRecipe as a player entity");
            return false;
        }

        String playerName = p.getName();
        ItemStack mainHand = p.getInventory().getItemInMainHand().clone();
        ItemStack offHand = p.getInventory().getItemInOffHand().clone();
        if (!passGuardCases(p, mainHand, offHand, args)) return false;

        mainHand.setAmount(1);
        offHand.setAmount(1);

        String recipeType = args[0];
        String returnRecipeName = args[1];

        ConfigManager.getManager().addRecipeToConfig(recipeType, returnRecipeName, mainHand, offHand, playerName);
        p.sendMessage(CraftReturnUtil.messageInfo(returnRecipeName + " has been added"));
        CraftReturn.getMain().getLogger().log(Level.INFO, p.getName() + " has added a " + recipeType + " CraftReturn recipe named " + returnRecipeName);
        CraftReturn.getMain().getLogger().log(Level.INFO, "Craft Item: " + mainHand.getType() + " | Return Item: " + offHand.getType());
        return true;
    }

    private boolean passGuardCases(Player p, ItemStack mainHand, ItemStack offHand, String[] args) {
        if (mainHand.getAmount() == 0) {
            p.sendMessage(CraftReturnUtil.messageError("You must hold an item in your main hand to represent the \"craft-item\""));
            return false;
        }
        if (offHand.getAmount() == 0) {
            p.sendMessage(CraftReturnUtil.messageError("You must hold an item in your off hand to represent the \"return-item\""));
            return false;
        }
        if (mainHand.getType() == offHand.getType()) {
            p.sendMessage(CraftReturnUtil.messageError("The \"craft-item\" and \"return-item\" must be different"));
            return false;
        }

        if (args.length < 1) {
            p.sendMessage(CraftReturnUtil.messageError("You must specify if this is a \"general\" or \"nbt\" recipe"));
            return false;
        }
        if (!ConfigManager.SUB_COMMANDS_RECIPE_TYPE.contains(args[0])) {
            p.sendMessage(CraftReturnUtil.messageError("You must specify if this is a \"general\" or \"nbt\" recipe"));
            return false;
        }
        if (args.length < 2) {
            p.sendMessage(CraftReturnUtil.messageError("You must provide a name for this return recipe(no spaces)"));
            return false;
        }

        if (args[0].equals("general") && ReturnableItemManager.getManager().getNamesGeneral().contains(args[1])) {
            p.sendMessage(CraftReturnUtil.messageError("A general recipe already has that name"));
            return false;
        }
        else if (args[0].equals("nbt") && ReturnableItemManager.getManager().getNamesNBT().contains(args[1])){
            p.sendMessage(CraftReturnUtil.messageError("A nbt recipe already has that name"));
            return false;
        }

        if (args[0].equals("general") && ReturnableItemManager.getManager().isGeneralReturnable(mainHand)) {
            p.sendMessage(CraftReturnUtil.messageError("There already exists a general recipe with that craft-item named \"" + ReturnableItemManager.getManager().getGeneralName(mainHand) + "\""));
            return false;
        }
        else if (args[0].equals("nbt") && ReturnableItemManager.getManager().isNBTReturnable(mainHand)) {
            p.sendMessage(CraftReturnUtil.messageError("There already exists a nbt recipe with that craft-item named \"" + ReturnableItemManager.getManager().getNBTName(mainHand) + "\""));
            return false;
        }

        return true;
    }
}