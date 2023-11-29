package me.whatahooda.itemreturnoncraft.models;

import me.whatahooda.itemreturnoncraft.models.registered.RegisteredReturnables;
import me.whatahooda.itemreturnoncraft.models.returnables.ReturnableItem;
import me.whatahooda.itemreturnoncraft.models.returnables.ReturnableItemGeneral;
import me.whatahooda.itemreturnoncraft.models.returnables.ReturnableItemNBT;
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
    //private Map<Material, ReturnableItemGeneral> registeredGeneralReturnables = new HashMap<>();
    private final RegisteredReturnables registeredGeneralReturnables = new RegisteredReturnables();
    /**
     * The currently registered NBT returnable items
     */
    //private Map<ItemStack, ReturnableItemNBT> registeredNBTReturnables = new HashMap<>();
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

    public String getGeneralName(ItemStack _stackToCheck) {
        return registeredGeneralReturnables.getName(_stackToCheck, false);
    }
    public String getNBTName(ItemStack _stackToCheck) {
        return registeredNBTReturnables.getName(_stackToCheck, true);
    }

    public ReturnableItem getGeneralRecipe(String _name) {
        return registeredGeneralReturnables.getReturnableItem(_name);
    }

    public ReturnableItem getNBTRecipe(String _name) {
        return registeredNBTReturnables.getReturnableItem(_name);
    }

    /**
     * Registers a general returnable item
     * @param _newCraftItem Craft item to initialize with
     * @param _newReturnItem Return item to initialize with
     */
    public void registerGeneralReturnable(String _name, ItemStack _newCraftItem, ItemStack _newReturnItem) {
        ReturnableItemGeneral newGeneralReturnable = new ReturnableItemGeneral(_newCraftItem, _newReturnItem);
        registeredGeneralReturnables.registerRecipe(_name, _newCraftItem, newGeneralReturnable);
    }

    /**
     * Registers a NBT returnable item
     * @param _newCraftItem Craft item to initialize with
     * @param _newReturnItem Return item to initialize with
     */
    public void registerNBTReturnable(String _name, ItemStack _newCraftItem, ItemStack _newReturnItem) {
        ReturnableItemNBT newNBTReturnable = new ReturnableItemNBT(_newCraftItem, _newReturnItem);
        registeredNBTReturnables.registerRecipe(_name, _newCraftItem, newNBTReturnable);
    }

    public boolean removeGeneralReturnable(String _recipeName) {
        return registeredGeneralReturnables.removeReturnableItem(_recipeName);
    }

    public boolean removeNBTReturnable(String _recipeName) {
        return registeredNBTReturnables.removeReturnableItem(_recipeName);
    }

    public boolean isGeneralReturnable(ItemStack _toCheck) {
        return (_toCheck != null) && registeredGeneralReturnables.isCraftItemRegistered(_toCheck, false);
    }

    public boolean isNBTReturnable(ItemStack _toCheck) {
        return (_toCheck != null) && registeredNBTReturnables.isCraftItemRegistered(_toCheck, true);
    }

    public List<ItemStack> getReturnItems(List<ItemStack> _nbtItems, List<ItemStack> _generalItems) {
        List<ItemStack> toReturn = new ArrayList<>();

        for (ItemStack _nbtStack : _nbtItems) {
            ItemStack temp = _nbtStack.clone();
            temp.setAmount(1);
            toReturn.add(registeredNBTReturnables.getReturnableItem(temp, true).getReturnItem().clone());
        }
        for (ItemStack _generalStack : _generalItems) {
            toReturn.add(registeredGeneralReturnables.getReturnableItem(_generalStack, false).getReturnItem().clone());
        }

        return toReturn;
    }

    public List<ItemStack> getReturnItemsShiftClick(CraftItemEvent _e, List<ItemStack> _nbtItems, List<ItemStack> _generalItems) {
        List<ItemStack> toReturn = getReturnItems(_nbtItems, _generalItems);
        int craftCount = getCraftCount(_e.getInventory(), _e.getWhoClicked().getInventory());

        for (ItemStack _toChange : toReturn) {
            _toChange.setAmount(_toChange.getAmount() * craftCount);
        }

        return toReturn;
    }

    private int getCraftCount(CraftingInventory _craftingInventory, Inventory _playerInventory) {
        int recipeResultCount = _craftingInventory.getResult().getAmount();
        int maxCanCraft = getMinStackCount(_craftingInventory.getMatrix()) * recipeResultCount;

        int playersFreeSpace = getPlayersFreeInventory(_playerInventory.getContents(), _craftingInventory.getResult());
        if (maxCanCraft > playersFreeSpace) maxCanCraft = playersFreeSpace - (playersFreeSpace % recipeResultCount);

        return maxCanCraft / recipeResultCount;
    }

    private int getMinStackCount(ItemStack[] _craftingMatrix) {
        int _minCount = -1;
        for (ItemStack _stack : _craftingMatrix) {
            if (_stack != null && (_stack.getAmount() < _minCount || _minCount == -1)) _minCount = _stack.getAmount();
        }
        return _minCount;
    }

    private int getPlayersFreeInventory(ItemStack[] _playerContents, ItemStack _recipeResult) {
        int totalFreeSpace = 0;
        //A semi magic number. This is the size and ending index of the standard storage indexes in a player inventory. Indexes 36+ is the armor and the offhand
        int PLAYER_INVENTORY_SIZE = 36;

        for (int i = 0; i < PLAYER_INVENTORY_SIZE; i++) {
            //if (_playerContents[i].getType() == Material.AIR) totalFreeSpace += _recipeResult.getMaxStackSize();
            if (_playerContents[i] == null) totalFreeSpace += _recipeResult.getMaxStackSize();
            else if (_playerContents[i].isSimilar(_recipeResult)) totalFreeSpace += _recipeResult.getMaxStackSize() - _playerContents[i].getAmount();
        }
        return totalFreeSpace;
    }
}
