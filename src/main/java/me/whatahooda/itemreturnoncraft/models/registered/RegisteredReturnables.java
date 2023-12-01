package me.whatahooda.itemreturnoncraft.models.registered;

import lombok.Getter;
import me.whatahooda.itemreturnoncraft.models.returnables.ReturnableItem;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class RegisteredReturnables {
    @Getter
    private final ArrayList<String> registeredNames = new ArrayList<>();
    private final ArrayList<ItemStack> craftItems = new ArrayList<>();
    private final ArrayList<ReturnableItem> registeredReturnableItems = new ArrayList<>();

    public void clear() {
        registeredNames.clear();
        craftItems.clear();
        registeredReturnableItems.clear();
    }

    /**
     * Checks if the provided ItemStack is registered in this instance
     * @param craftItemToCheck ItemStack to check
     * @param checkNBT If set to true will search using NBT data, if false will search using Material
     * @return True if the ItemStack was found, false if not
     */
    public boolean isCraftItemRegistered(ItemStack craftItemToCheck, boolean checkNBT) {
        for (ItemStack toCompare : craftItems) {
            if (checkNBT) {
                if (craftItemToCheck.isSimilar(toCompare)) return true;
            }
            else {
                if (craftItemToCheck.getType() == toCompare.getType()) return true;
            }
        }
        return false;
    }

    /**
     * Adds a recipe to this instance
     * @param name Name of the recipe
     * @param craftItem The craftItem of the recipe
     * @param returnableItem The ReturnableItem instance that represents the recipe
     */
    public void registerRecipe(String name, ItemStack craftItem, ReturnableItem returnableItem) {
        registeredNames.add(name);
        craftItems.add(craftItem);
        registeredReturnableItems.add(returnableItem);
    }

    /**
     * Removes a recipe
     *
     * @param recipeName The name of the recipe you want to remove
     */
    public void removeReturnableItem(String recipeName) {
        for (int i = 0; i < registeredNames.size(); i++) {
            if (recipeName.equals(registeredNames.get(i))) {
                registeredNames.remove(i);
                craftItems.remove(i);
                registeredReturnableItems.remove(i);
                return;
            }
        }
    }

    /**
     * Gets the ReturnableItem instance
     * @param recipeName Name of the recipe to retrieve
     * @return ReturnableItem that corresponds to the name provided, null if no recipe was found
     */
    public ReturnableItem getReturnableItem(String recipeName) {
        for (int i = 0; i < registeredNames.size(); i++) {
            if (recipeName.equals(registeredNames.get(i))) return registeredReturnableItems.get(i);
        }
        return null;
    }

    /**
     * Gets the ReturnableItem instance
     * @param craftItem ItemStack to check for
     * @param checkNBT If set to true will search using NBT data, if false will search using Material
     * @return ReturnableItem that corresponds to the ItemStack provided, null if no recipe was found
     */
    public ReturnableItem getReturnableItem(ItemStack craftItem, boolean checkNBT) {
        for (int i = 0; i < registeredNames.size(); i++) {
            if (checkNBT) {
                if (craftItem.isSimilar(craftItems.get(i))) return registeredReturnableItems.get(i);
            }
            else {
                if (craftItem.getType() == craftItems.get(i).getType()) return registeredReturnableItems.get(i);
            }
        }
        return null;
    }

    /**
     * Gets the ReturnableItem instance
     * @param craftItem ItemStack to check for
     * @param checkNBT If set to true will search using NBT data, if false will search using Material
     * @return String that corresponds to the ItemStack provided, null if no recipe was found
     */
    public String getName(ItemStack craftItem, boolean checkNBT) {
        for (int i = 0; i < registeredNames.size(); i++) {
            if (checkNBT) {
                if (craftItem.isSimilar(craftItems.get(i))) return registeredNames.get(i);
            }
            else {
                if (craftItem.getType() == craftItems.get(i).getType()) return registeredNames.get(i);
            }
        }
        return null;
    }
}
