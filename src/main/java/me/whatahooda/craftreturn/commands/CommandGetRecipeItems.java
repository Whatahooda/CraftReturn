package me.whatahooda.craftreturn.commands;

import me.whatahooda.craftreturn.CraftReturn;
import me.whatahooda.craftreturn.config.ConfigManager;
import me.whatahooda.craftreturn.models.ReturnableItemManager;
import me.whatahooda.craftreturn.models.returnables.ReturnableItem;
import me.whatahooda.craftreturn.util.CraftReturnUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class CommandGetRecipeItems implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("craftreturn.command.getrecipe")) {
            sender.sendMessage(CraftReturnUtil.COMMAND_NO_PERMISSION);
            return true;
        }
        if (!(sender instanceof Player p)) {
            CraftReturn.getMain().getLogger().log(Level.WARNING, "You must execute getRecipe as a player entity");
            return false;
        }

        if (!passGuardCases(p, args)) return false;

        String type = args[0];
        String recipeName = args[1];

        if (type.equals("general")) {
            ReturnableItem recipe = ReturnableItemManager.getManager().getGeneralRecipe(recipeName);
            p.getInventory().setItemInMainHand(recipe.getCraftItem());
            p.getInventory().setItemInOffHand(recipe.getReturnItem());
        }
        else if (type.equals("nbt")) {
            ReturnableItem recipe = ReturnableItemManager.getManager().getNBTRecipe(recipeName);
            p.getInventory().setItemInMainHand(recipe.getCraftItem());
            p.getInventory().setItemInOffHand(recipe.getReturnItem());
        }

        return true;
    }

    private boolean passGuardCases(Player p, String[] args) {
        if (args.length == 0 || !ConfigManager.SUB_COMMANDS_RECIPE_TYPE.contains(args[0])) {
            p.sendMessage(CraftReturnUtil.messageError("You must specify if this is a \"general\" or \"nbt\" recipe"));
            return false;
        }
        if (args.length < 2) {
            p.sendMessage(CraftReturnUtil.messageError("You must provide a recipe name"));
            return false;
        }
        if (args[0].equals("general") && !ReturnableItemManager.getManager().getNamesGeneral().contains(args[1])) {
            p.sendMessage(CraftReturnUtil.messageError("You must provide a \"general\" recipe name"));
            return false;
        }
        else if (args[0].equals("nbt") && !ReturnableItemManager.getManager().getNamesNBT().contains(args[1])){
            p.sendMessage(CraftReturnUtil.messageError("You must provide a \"nbt\" recipe name"));
            return false;
        }

        if (args.length < 3 || !args[2].equals("confirm")) {
            p.sendMessage(CraftReturnUtil.messageError("This will REPLACE the items in your main and off hand, please confirm"));
            return false;
        }

        return true;
    }
}
