package me.whatahooda.craftreturn.listeners;

import me.whatahooda.craftreturn.models.ReturnableItemManager;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListenerCraftItemEvent implements Listener {

    @EventHandler
    public void OnItemCraft(CraftItemEvent craftEvent) {

        List<ItemStack> itemsReturnableNBT = new ArrayList<>();
        List<ItemStack> itemsReturnableGeneral = new ArrayList<>();

        for (ItemStack _stack : craftEvent.getInventory().getMatrix()) {
            if (ReturnableItemManager.getManager().isNBTReturnable(_stack)) itemsReturnableNBT.add(_stack);
            else if (ReturnableItemManager.getManager().isGeneralReturnable(_stack)) itemsReturnableGeneral.add(_stack);
        }

        if (!passGuardCases(craftEvent, itemsReturnableNBT.size() + itemsReturnableGeneral.size())) return;

        List<ItemStack> itemsToReturn;

        if (craftEvent.isShiftClick()) {
            itemsToReturn = ReturnableItemManager.getManager().getReturnItemsShiftClick(craftEvent, itemsReturnableNBT, itemsReturnableGeneral);
        }
        else {
            itemsToReturn = ReturnableItemManager.getManager().getReturnItems(itemsReturnableNBT, itemsReturnableGeneral);
        }

        returnItems(craftEvent.getWhoClicked(), itemsToReturn);
    }

    private boolean passGuardCases(CraftItemEvent e, int returnableListLength) {
        if (returnableListLength == 0) return false;

        if (e.getWhoClicked().getItemOnCursor().getType() != Material.AIR) {
            if (e.getInventory().getResult().getType() != e.getWhoClicked().getItemOnCursor().getType()) return false;

            if (!e.isLeftClick() && !e.isRightClick() && !e.isShiftClick()) return false;

            if ((e.isRightClick() || e.isLeftClick()) &&
                    e.getWhoClicked().getItemOnCursor().getAmount() + e.getInventory().getResult().getAmount() >= e.getWhoClicked().getItemOnCursor().getMaxStackSize())
                return false;
        }

        return true;
    }

    private void returnItems(HumanEntity player, List<ItemStack> itemsToReturn) {
        for (ItemStack _stack : itemsToReturn) {
            HashMap<Integer, ItemStack> leftOver = player.getInventory().addItem(_stack);
            if (!leftOver.isEmpty()) playerDropItemStack(player, leftOver.get(0));
        }
    }

    /**
     * Generates an item stack and drops it from the players inventory
     * @param player Human entity we use
     * @param stackToDrop The item stack that will be created and dropped
     */
    private void playerDropItemStack(HumanEntity player, ItemStack stackToDrop) {
        ItemStack oldHand = player.getInventory().getItemInMainHand();
        // Seems a bit sketchy to delete and replace the main hand item
        // I couldn't find another way to perfectly imitate "over crafting" or "drop crafting"
        player.getInventory().setItemInMainHand(stackToDrop);
        player.dropItem(true);
        player.getInventory().setItemInMainHand(oldHand);
    }
}
