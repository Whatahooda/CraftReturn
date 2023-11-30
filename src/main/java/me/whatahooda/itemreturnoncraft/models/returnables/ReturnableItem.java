package me.whatahooda.itemreturnoncraft.models.returnables;

import org.bukkit.inventory.ItemStack;

public interface ReturnableItem {

    /**
     * Checks if the stackToCheck is the same as this craftItem.
     * @param stackToCheck ItemStack to check against this.itemCraft
     * @return Returns true if stackToCheck matches with this.itemCraft, false otherwise
     */
    boolean isItemReturnable(ItemStack stackToCheck);

    /**
     * Get this.itemCraft
     * @return ItemStack
     */
    ItemStack getCraftItem();

    /**
     * Get this.itemReturn
     * @return ItemStack
     */
    ItemStack getReturnItem();

    /**
     * Changes itemToChange to be similar to itemFrom
     * @param itemToChange The ItemStack to change
     * @param itemFrom The ItemStack we copy data from
     */
    void setItem(ItemStack itemToChange, ItemStack itemFrom);
}