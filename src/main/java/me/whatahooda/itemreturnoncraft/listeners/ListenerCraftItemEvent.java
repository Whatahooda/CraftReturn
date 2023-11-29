package me.whatahooda.itemreturnoncraft.listeners;

import me.whatahooda.itemreturnoncraft.models.ReturnableItemManager;
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
    public void OnItemCraft(CraftItemEvent _craftEvent) {

        List<ItemStack> itemsReturnableNBT = new ArrayList<>();
        List<ItemStack> itemsReturnableGeneral = new ArrayList<>();

        for (ItemStack _stack : _craftEvent.getInventory().getMatrix()) {
            if (ReturnableItemManager.getManager().isNBTReturnable(_stack)) itemsReturnableNBT.add(_stack);
            else if (ReturnableItemManager.getManager().isGeneralReturnable(_stack)) itemsReturnableGeneral.add(_stack);
        }

        if (!passGuardCases(_craftEvent, itemsReturnableNBT.size() + itemsReturnableGeneral.size())) return;

        List<ItemStack> itemsToReturn;

        if (_craftEvent.isShiftClick()) {
            itemsToReturn = ReturnableItemManager.getManager().getReturnItemsShiftClick(_craftEvent, itemsReturnableNBT, itemsReturnableGeneral);
        }
        else {
            itemsToReturn = ReturnableItemManager.getManager().getReturnItems(itemsReturnableNBT, itemsReturnableGeneral);
        }

        returnItems(_craftEvent.getWhoClicked(), itemsToReturn);
    }

    private boolean passGuardCases(CraftItemEvent _e, int _returnableListLength) {
        if (_returnableListLength == 0) return false;

        if (_e.getWhoClicked().getItemOnCursor().getType() != Material.AIR) {
            if (_e.getInventory().getResult().getType() != _e.getWhoClicked().getItemOnCursor().getType()) return false;

            if (!_e.isLeftClick() && !_e.isRightClick() && !_e.isShiftClick()) return false;

            if ((_e.isRightClick() || _e.isLeftClick()) &&
                    _e.getWhoClicked().getItemOnCursor().getAmount() + _e.getInventory().getResult().getAmount() >= _e.getWhoClicked().getItemOnCursor().getMaxStackSize())
                return false;
        }

        return true;
    }

    private void returnItems(HumanEntity _player, List<ItemStack> _itemsToReturn) {
        for (ItemStack _stack : _itemsToReturn) {
            HashMap<Integer, ItemStack> leftOver = _player.getInventory().addItem(_stack);
            if (leftOver.size() > 0) playerDropItemStack(_player, leftOver.get(0));
        }
    }

    /**
     * Generates an item stack and drops it from the players inventory
     * @param _player Human entity we use
     * @param _stackToDrop The item stack that will be created and dropped
     */
    private void playerDropItemStack(HumanEntity _player, ItemStack _stackToDrop) {
        ItemStack oldHand = _player.getInventory().getItemInMainHand();
        // Seems a bit sketchy to delete and replace the main hand item
        // I couldn't find another way to perfectly imitate "over crafting" or "drop crafting"
        _player.getInventory().setItemInMainHand(_stackToDrop);
        _player.dropItem(true);
        _player.getInventory().setItemInMainHand(oldHand);
    }
}
