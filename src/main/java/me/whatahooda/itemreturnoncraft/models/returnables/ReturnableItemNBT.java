package me.whatahooda.itemreturnoncraft.models.returnables;

import org.bukkit.inventory.ItemStack;

public class ReturnableItemNBT implements ReturnableItem{
    private final ItemStack craftItem;
    private final ItemStack returnItem;

    public ReturnableItemNBT(ItemStack _newCraftItem, ItemStack _newReturnItem) {
        this.craftItem = _newCraftItem;
        this.returnItem = _newReturnItem;
    }

    @Override
    public boolean isItemReturnable(ItemStack _itemToCheck) {
        return this.craftItem.isSimilar(_itemToCheck);
    }

    @Override
    public ItemStack getCraftItem() { return craftItem; }

    @Override
    public ItemStack getReturnItem() {
        return this.returnItem;
    }

    @Override
    public void setItem(ItemStack _itemToChange, ItemStack _itemFrom) {
        _itemToChange.setType(_itemFrom.getType());
        _itemToChange.setData(_itemFrom.getData());
        _itemToChange.setItemMeta(_itemFrom.getItemMeta());
    }
}
