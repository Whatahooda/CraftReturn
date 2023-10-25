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

    // Forces player to drop items
    private void PlayerDropItemStack(HumanEntity _player, ItemStack _stack_to_drop) {
        ItemStack old_hand = _player.getInventory().getItemInMainHand();
        // Seems a bit sketchy to delete and replace the main hand item
        // I couldn't find another way to perfectly imitate "over crafting" or "drop crafting"
        _player.getInventory().setItemInMainHand(_stack_to_drop);
        _player.dropItem(true);
        _player.getInventory().setItemInMainHand(old_hand);
    }

    @EventHandler
    public void OnItemCraft(CraftItemEvent craft_event) {

        // Do I have to initialize this everytime the event is called? Idk how to do it otherwise yet TODO Get java knowledge
        HashMap<Material, Material> returned_materials = new HashMap<>();
        returned_materials.put(Material.POTION, Material.GLASS_BOTTLE);
        returned_materials.put(Material.SPLASH_POTION, Material.GLASS_BOTTLE);
        returned_materials.put(Material.LINGERING_POTION, Material.GLASS_BOTTLE);

        int result_stack_amount = craft_event.getRecipe().getResult().getAmount();

        // Return if this is not a recipe we care about
        boolean skip_recipe = true;
        // We will use this array later
        ItemStack[] crafting_inventory_matrix = craft_event.getInventory().getMatrix();
        for (final ItemStack itemStack : crafting_inventory_matrix) {
            if (itemStack != null && returned_materials.containsKey(itemStack.getType())) skip_recipe = false;
        }
        if (skip_recipe) return;

        // Used to track items that couldn't fit in player inventory on "shift craft"
        // Using remainder may be redundant? In testing, it seemed the result stack count was changed by .addItem()
        HashMap<Integer, ItemStack> remainder = new HashMap<>();

        boolean is_click_craft = craft_event.isLeftClick() || craft_event.isRightClick();
        boolean is_shift_craft = craft_event.isShiftClick();

        // On shift click make sure there is room in the inventory
        if (is_shift_craft) {
            ItemStack result = craft_event.getRecipe().getResult();
            // Attempt to give the player the crafting result
            remainder = craft_event.getWhoClicked().getInventory().addItem(result);

            // If there was no room to add items to the player inventory, cancel
            if (!remainder.isEmpty() && remainder.get(0).getAmount() == result_stack_amount) {
                craft_event.setResult(Event.Result.DENY);
                return;
            }
        }
        // On normal click make sure we can add to cursor stack
        else if (is_click_craft) {
            ItemStack cursor_stack = craft_event.getWhoClicked().getItemOnCursor();
            ItemStack result_stack = craft_event.getRecipe().getResult();
            // Don't craft if the cursor is not holding the right material, or is too full
            if (cursor_stack.getType() != Material.AIR && (cursor_stack.getType() != result_stack.getType() || cursor_stack.getAmount() + result_stack.getAmount() > cursor_stack.getMaxStackSize())) {
                craft_event.setResult(Event.Result.DENY);
                return;
            }
        }


        // Handle each item in the crafting inventory
        for (ItemStack stack : crafting_inventory_matrix) {
            if (stack == null) continue;

            // Convert items we care about into their returned items
            if (returned_materials.containsKey(stack.getType())) {
                stack.setType(returned_materials.get(stack.getType()));
                continue;
            }

            // Otherwise properly use up materials
            int new_amount = stack.getAmount() - 1;
            if (new_amount > 0) stack.setAmount(new_amount);
            else stack.setType(Material.AIR);
        }

        // Update the crafting inventory with returned items and updated item stacks
        craft_event.getInventory().setMatrix(crafting_inventory_matrix);


        // If we tried to shift click craft and there was a remainder, drop the items on the ground
        if(is_shift_craft) {
            if (!remainder.isEmpty()) PlayerDropItemStack(craft_event.getWhoClicked(), remainder.get(0));
            return;
        }
        // Add result stack to cursor on right or left click (must be done after .setMatrix())
        else if (is_click_craft) {
            // Combine items on cursor with result items
            ItemStack cursor_stack = craft_event.getWhoClicked().getItemOnCursor();
            ItemStack result_stack = craft_event.getRecipe().getResult();
            if (cursor_stack.getType() == result_stack.getType()) {
                craft_event.getWhoClicked().setItemOnCursor(new ItemStack (result_stack.getType(), result_stack.getAmount() + cursor_stack.getAmount()));
            }
            else craft_event.getWhoClicked().setItemOnCursor(result_stack);
            return;
        }

        // Drop crafting/event wasn't initiated with any click
        PlayerDropItemStack(craft_event.getWhoClicked(), craft_event.getRecipe().getResult());
    }
}
