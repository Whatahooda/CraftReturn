package me.whatahooda.itemreturnoncraft.models.returnables;

import org.bukkit.inventory.ItemStack;

public class ReturnableItemGeneral implements ReturnableItem{
    private final ItemStack craftItem;
    private final ItemStack returnItem;

    public ReturnableItemGeneral(ItemStack newCraftItem, ItemStack newReturnItem) {
        this.craftItem = newCraftItem;
        this.returnItem = newReturnItem;
    }

    @Override
    public boolean isItemReturnable(ItemStack itemToCheck) {
        return this.craftItem.getType() == itemToCheck.getType();
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
