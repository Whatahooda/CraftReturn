package me.whatahooda.itemreturnoncraft.models.registered;

import me.whatahooda.itemreturnoncraft.models.returnables.ReturnableItem;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class RegisteredReturnables {
    private final ArrayList<String> registeredNames = new ArrayList<>();
    private final ArrayList<ItemStack> craftItems = new ArrayList<>();
    private final ArrayList<ReturnableItem> registeredReturnableItems = new ArrayList<>();

    public ArrayList<String> getRegisteredNames() {return registeredNames;}
    public ArrayList<ItemStack> getCraftItems() {return craftItems;}
    public ArrayList<ReturnableItem> getregisteredReturnableItems() {return registeredReturnableItems;}

    public void clear() {
        registeredNames.clear();
        craftItems.clear();
        registeredReturnableItems.clear();
    }

    /**
     * Checks if the provided ItemStack is registered in this instance
     * @param _craftItemToCheck ItemStack to check
     * @param _checkNBT If set to true will search using NBT data, if false will search using Material
     * @return True if the ItemStack was found, false if not
     */
    public boolean isCraftItemRegistered(ItemStack _craftItemToCheck, boolean _checkNBT) {
        for (ItemStack _toCompare : craftItems) {
            if (_checkNBT) {
                if (_craftItemToCheck.isSimilar(_toCompare)) return true;
            }
            else {
                if (_craftItemToCheck.getType() == _toCompare.getType()) return true;
            }
        }
        return false;
    }

    /**
     * Adds a recipe to this instance
     * @param _name Name of the recipe
     * @param _craftItem The craftItem of the recipe
     * @param _returnableItem The ReturnableItem instance that represents the recipe
     */
    public void registerRecipe(String _name, ItemStack _craftItem, ReturnableItem _returnableItem) {
        registeredNames.add(_name);
        craftItems.add(_craftItem);
        registeredReturnableItems.add(_returnableItem);
    }

    /**
     * Removes a recipe
     * @param _recipeName The name of the recipe you want to remove
     * @return True if the recipe was found and removed, false if the name does not match with a recipe
     */
    public boolean removeReturnableItem(String _recipeName) {
        for (int i = 0; i < registeredNames.size(); i++) {
            if (_recipeName.equals(registeredNames.get(i))) {
                registeredNames.remove(i);
                craftItems.remove(i);
                registeredReturnableItems.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the ReturnableItem instance
     * @param _recipeName Name of the recipe to retrieve
     * @return ReturnableItem that corresponds to the name provided, null if no recipe was found
     */
    public ReturnableItem getReturnableItem(String _recipeName) {
        for (int i = 0; i < registeredNames.size(); i++) {
            if (_recipeName.equals(registeredNames.get(i))) return registeredReturnableItems.get(i);
        }
        return null;
    }

    /**
     * Gets the ReturnableItem instance
     * @param _craftItem ItemStack to check for
     * @param _checkNBT If set to true will search using NBT data, if false will search using Material
     * @return ReturnableItem that corresponds to the ItemStack provided, null if no recipe was found
     */
    public ReturnableItem getReturnableItem(ItemStack _craftItem, boolean _checkNBT) {
        for (int i = 0; i < registeredNames.size(); i++) {
            if (_checkNBT) {
                if (_craftItem.isSimilar(craftItems.get(i))) return registeredReturnableItems.get(i);
            }
            else {
                if (_craftItem.getType() == craftItems.get(i).getType()) return registeredReturnableItems.get(i);
            }
        }
        return null;
    }

    /**
     * Gets the ReturnableItem instance
     * @param _craftItem ItemStack to check for
     * @param _checkNBT If set to true will search using NBT data, if false will search using Material
     * @return String that corresponds to the ItemStack provided, null if no recipe was found
     */
    public String getName(ItemStack _craftItem, boolean _checkNBT) {
        for (int i = 0; i < registeredNames.size(); i++) {
            if (_checkNBT) {
                if (_craftItem.isSimilar(craftItems.get(i))) return registeredNames.get(i);
            }
            else {
                if (_craftItem.getType() == craftItems.get(i).getType()) return registeredNames.get(i);
            }
        }
        return null;
    }
}
