package me.whatahooda.itemreturnoncraft;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class RecipeReturnsBottle implements  Listener {

    private final HashMap<Material, Material> RETURNED_MATERIALS = new HashMap<>();
    public RecipeReturnsBottle() {
        RETURNED_MATERIALS.put(Material.POTION, Material.GLASS_BOTTLE);
        RETURNED_MATERIALS.put(Material.SPLASH_POTION, Material.GLASS_BOTTLE);
        RETURNED_MATERIALS.put(Material.LINGERING_POTION, Material.GLASS_BOTTLE);
    }

    // Forces player to drop items
    private void playerDropItemStack(HumanEntity _player, ItemStack _stackToDrop) {
        ItemStack oldHand = _player.getInventory().getItemInMainHand();
        // Seems a bit sketchy to delete and replace the main hand item
        // I couldn't find another way to perfectly imitate "over crafting" or "drop crafting"
        _player.getInventory().setItemInMainHand(_stackToDrop);
        _player.dropItem(true);
        _player.getInventory().setItemInMainHand(oldHand);
    }

    @EventHandler
    public void OnItemCraft(CraftItemEvent _craftEvent) {
        int resultStackAmount = _craftEvent.getRecipe().getResult().getAmount();

        // Return if this is not a recipe we care about
        boolean skipRecipe = true;
        // We will use this array later
        ItemStack[] craftingInventoryMatrix = _craftEvent.getInventory().getMatrix();
        for (final ItemStack _itemStack : craftingInventoryMatrix) {
            if (_itemStack != null && RETURNED_MATERIALS.containsKey(_itemStack.getType())) skipRecipe = false;
        }
        if (skipRecipe) return;

        // Used to track items that couldn't fit in player inventory on "shift craft"
        // Using remainder may be redundant? In testing, it seemed the result stack count was changed by .addItem()
        HashMap<Integer, ItemStack> remainder = new HashMap<>();

        boolean isClickCraft = _craftEvent.isLeftClick() || _craftEvent.isRightClick();
        boolean isShiftCraft = _craftEvent.isShiftClick();

        // On shift click make sure there is room in the inventory
        if (isShiftCraft) {
            ItemStack result = _craftEvent.getRecipe().getResult();
            // Attempt to give the player the crafting result
            remainder = _craftEvent.getWhoClicked().getInventory().addItem(result);

            // If there was no room to add items to the player inventory, cancel
            if (!remainder.isEmpty() && remainder.get(0).getAmount() == resultStackAmount) {
                _craftEvent.setResult(Event.Result.DENY);
                return;
            }
        }
        // On normal click make sure we can add to cursor stack
        else if (isClickCraft) {
            ItemStack cursorStack = _craftEvent.getWhoClicked().getItemOnCursor();
            ItemStack resultStack = _craftEvent.getRecipe().getResult();
            // Don't craft if the cursor is not holding the right material, or is too full
            if (cursorStack.getType() != Material.AIR && (cursorStack.getType() != resultStack.getType() || cursorStack.getAmount() + resultStack.getAmount() > cursorStack.getMaxStackSize())) {
                _craftEvent.setResult(Event.Result.DENY);
                return;
            }
        }


        // Handle each item in the crafting inventory
        for (ItemStack _stack : craftingInventoryMatrix) {
            if (_stack == null) continue;

            // Convert items we care about into their returned items
            if (RETURNED_MATERIALS.containsKey(_stack.getType())) {
                _stack.setType(RETURNED_MATERIALS.get(_stack.getType()));
                continue;
            }

            // Otherwise properly use up materials
            int new_amount = _stack.getAmount() - 1;
            if (new_amount > 0) _stack.setAmount(new_amount);
            else _stack.setType(Material.AIR);
        }

        // Update the crafting inventory with returned items and updated item stacks
        _craftEvent.getInventory().setMatrix(craftingInventoryMatrix);


        // If we tried to shift click craft and there was a remainder, drop the items on the ground
        if(isShiftCraft) {
            if (!remainder.isEmpty()) playerDropItemStack(_craftEvent.getWhoClicked(), remainder.get(0));
            return;
        }
        // Add result stack to cursor on right or left click (must be done after .setMatrix())
        else if (isClickCraft) {
            // Combine items on cursor with result items
            ItemStack cursorStack = _craftEvent.getWhoClicked().getItemOnCursor();
            ItemStack resultStack = _craftEvent.getRecipe().getResult();
            if (cursorStack.getType() == resultStack.getType()) {
                _craftEvent.getWhoClicked().setItemOnCursor(new ItemStack (resultStack.getType(), resultStack.getAmount() + cursorStack.getAmount()));
            }
            else _craftEvent.getWhoClicked().setItemOnCursor(resultStack);
            return;
        }

        // Drop crafting/event wasn't initiated with any click
        playerDropItemStack(_craftEvent.getWhoClicked(), _craftEvent.getRecipe().getResult());
    }
}
