package me.whatahooda.itemreturnoncraft.models.returnables;

import org.bukkit.inventory.ItemStack;

public interface ReturnableItem {

    /**
     * Checks if the _stackToCheck is the same as this craftItem.
     * @param _stackToCheck ItemStack to check against this.itemCraft
     * @return Returns true if _stackToCheck matches with this.itemCraft, false otherwise
     */
    boolean isItemReturnable(ItemStack _stackToCheck);

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
     * Changes _itemToChange to be similar to _itemFrom
     * @param _itemToChange The ItemStack to change
     * @param _itemFrom The ItemStack we copy data from
     */
    void setItem(ItemStack _itemToChange, ItemStack _itemFrom);
}