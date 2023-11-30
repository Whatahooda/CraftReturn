package me.whatahooda.itemreturnoncraft.models.returnables;

import org.bukkit.inventory.ItemStack;

public class ReturnableItemNBT implements ReturnableItem{
    private final ItemStack craftItem;
    private final ItemStack returnItem;

    public ReturnableItemNBT(ItemStack newCraftItem, ItemStack newReturnItem) {
        this.craftItem = newCraftItem;
        this.returnItem = newReturnItem;
    }

    @Override
    public boolean isItemReturnable(ItemStack itemToCheck) {
        return this.craftItem.isSimilar(itemToCheck);
    }

    @Override
    public ItemStack getCraftItem() { return craftItem; }

    @Override
    public ItemStack getReturnItem() {
        return this.returnItem;
    }

    @Override
    public void setItem(ItemStack itemToChange, ItemStack itemFrom) {
        itemToChange.setType(itemFrom.getType());
        itemToChange.setData(itemFrom.getData());
        itemToChange.setItemMeta(itemFrom.getItemMeta());
    }
}
