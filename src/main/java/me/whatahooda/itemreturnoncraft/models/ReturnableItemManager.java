package me.whatahooda.itemreturnoncraft.models;

import me.whatahooda.itemreturnoncraft.models.registered.RegisteredReturnables;
import me.whatahooda.itemreturnoncraft.models.returnables.ReturnableItem;
import me.whatahooda.itemreturnoncraft.models.returnables.ReturnableItemGeneral;
import me.whatahooda.itemreturnoncraft.models.returnables.ReturnableItemNBT;
import org.bukkit.Material;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ReturnableItemManager {
    /**
     * Singleton instance of the returnable item manager
     */
    private static ReturnableItemManager managerInstance;

    /**
     * The currently registered general returnable items
     */
    private final RegisteredReturnables registeredGeneralReturnables = new RegisteredReturnables();
    /**
     * The currently registered NBT returnable items
     */
    private final RegisteredReturnables registeredNBTReturnables = new RegisteredReturnables();

    /**
     * Gets the instance of the returnable manager, singleton shenanigans!
     * @return ReturnableItemManager
     */
    public static ReturnableItemManager getManager() {
        if (managerInstance == null) {
            managerInstance = new ReturnableItemManager();
        }
        return managerInstance;
    }

    public ArrayList<String> getNamesGeneral() { return registeredGeneralReturnables.getRegisteredNames(); }
    public ArrayList<String> getNamesNBT() { return registeredNBTReturnables.getRegisteredNames(); }

    public void clearRegisteredItems() {
        registeredNBTReturnables.clear();
        registeredGeneralReturnables.clear();
    }

    public String getGeneralName(ItemStack stackToCheck) {
        return registeredGeneralReturnables.getName(stackToCheck, false);
    }
    public String getNBTName(ItemStack stackToCheck) {
        return registeredNBTReturnables.getName(stackToCheck, true);
    }

    public ReturnableItem getGeneralRecipe(String name) {
        return registeredGeneralReturnables.getReturnableItem(name);
    }

    public ReturnableItem getNBTRecipe(String name) {
        return registeredNBTReturnables.getReturnableItem(name);
    }

    /**
     * Registers a general returnable item
     * @param newCraftItem Craft item to initialize with
     * @param newReturnItem Return item to initialize with
     */
    public void registerGeneralReturnable(String name, ItemStack newCraftItem, ItemStack newReturnItem) {
        ReturnableItemGeneral newGeneralReturnable = new ReturnableItemGeneral(newCraftItem, newReturnItem);
        registeredGeneralReturnables.registerRecipe(name, newCraftItem, newGeneralReturnable);
    }

    /**
     * Registers a NBT returnable item
     * @param newCraftItem Craft item to initialize with
     * @param newReturnItem Return item to initialize with
     */
    public void registerNBTReturnable(String name, ItemStack newCraftItem, ItemStack newReturnItem) {
        ReturnableItemNBT newNBTReturnable = new ReturnableItemNBT(newCraftItem, newReturnItem);
        registeredNBTReturnables.registerRecipe(name, newCraftItem, newNBTReturnable);
    }

    public void removeGeneralReturnable(String recipeName) {
        registeredGeneralReturnables.removeReturnableItem(recipeName);
    }

    public void removeNBTReturnable(String recipeName) {
        registeredNBTReturnables.removeReturnableItem(recipeName);
    }

    public boolean isGeneralReturnable(ItemStack toCheck) {
        return (toCheck != null) && registeredGeneralReturnables.isCraftItemRegistered(toCheck, false);
    }

    public boolean isNBTReturnable(ItemStack toCheck) {
        return (toCheck != null) && registeredNBTReturnables.isCraftItemRegistered(toCheck, true);
    }

    public List<ItemStack> getReturnItems(List<ItemStack> nbtItems, List<ItemStack> generalItems) {
        List<ItemStack> toReturn = new ArrayList<>();

        for (ItemStack nbtStack : nbtItems) {
            ItemStack temp = nbtStack.clone();
            temp.setAmount(1);
            toReturn.add(registeredNBTReturnables.getReturnableItem(temp, true).getReturnItem().clone());
        }
        for (ItemStack generalStack : generalItems) {
            toReturn.add(registeredGeneralReturnables.getReturnableItem(generalStack, false).getReturnItem().clone());
        }

        return toReturn;
    }

    public List<ItemStack> getReturnItemsShiftClick(CraftItemEvent e, List<ItemStack> nbtItems, List<ItemStack> generalItems) {
        List<ItemStack> toReturn = getReturnItems(nbtItems, generalItems);
        int craftCount = getCraftCount(e.getInventory(), e.getWhoClicked().getInventory());

        for (ItemStack toChange : toReturn) {
            toChange.setAmount(toChange.getAmount() * craftCount);
        }

        return toReturn;
    }

    private int getCraftCount(CraftingInventory craftingInventory, Inventory playerInventory) {
        int recipeResultCount = craftingInventory.getResult().getAmount();
        int maxCanCraft = getMinStackCount(craftingInventory.getMatrix()) * recipeResultCount;

        int playersFreeSpace = getPlayersFreeInventory(playerInventory.getContents(), craftingInventory.getResult());
        if (maxCanCraft > playersFreeSpace) maxCanCraft = playersFreeSpace - (playersFreeSpace % recipeResultCount);

        return maxCanCraft / recipeResultCount;
    }

    private int getMinStackCount(ItemStack[] craftingMatrix) {
        int minCount = -1;
        for (ItemStack stack : craftingMatrix) {
            if (stack != null && (stack.getAmount() < minCount || minCount == -1)) minCount = stack.getAmount();
        }
        return minCount;
    }

    private int getPlayersFreeInventory(ItemStack[] playerContents, ItemStack recipeResult) {
        int totalFreeSpace = 0;
        //A semi magic number. This is the size and ending index of the standard storage indexes in a player inventory. Indexes 36+ are the armor slots and offhand slot
        int playerInventorySize = 36;

        for (int i = 0; i < playerInventorySize; i++) {
            if (playerContents[i] == null || playerContents[i].getType() == Material.AIR) totalFreeSpace += recipeResult.getMaxStackSize();
            else if (playerContents[i].isSimilar(recipeResult)) totalFreeSpace += recipeResult.getMaxStackSize() - playerContents[i].getAmount();
        }
        return totalFreeSpace;
    }
}
