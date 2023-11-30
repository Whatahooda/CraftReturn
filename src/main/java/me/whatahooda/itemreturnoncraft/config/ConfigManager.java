package me.whatahooda.itemreturnoncraft.config;

import me.whatahooda.itemreturnoncraft.ItemReturnOnCraft;
import me.whatahooda.itemreturnoncraft.models.ReturnableItemManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Level;

public class ConfigManager {

    private static ConfigManager managerInstance;
    private static final FileConfiguration CONFIG = ItemReturnOnCraft.getMain().getConfig();

    public static final String CONFIG_SECTION_WORKBENCH = "returns-workbench";
    public static final String CONFIG_SECTION_GENERAL = "general";
    public static final String CONFIG_SECTION_NBT = "nbt";
    public static final String CONFIG_SECTION_CRAFT_ITEM = "craft-item";
    public static final String CONFIG_SECTION_RETURN_ITEM = "return-item";

    public static final ArrayList<String> SUB_COMMANDS_RECIPE_TYPE = new ArrayList<>(Arrays.asList("general", "nbt"));


    public static ConfigManager getManager() {
        if (managerInstance == null) {
            managerInstance = new ConfigManager();
        }
        return managerInstance;
    }

    public void addRecipeToConfig(String _recipeType, String _sectionName, ItemStack _craftItem, ItemStack _returnItem, String _whoAdded) {
        LinkedHashMap<String, ItemStack> pathValueMap = new LinkedHashMap<>();
        pathValueMap.put(CONFIG_SECTION_CRAFT_ITEM, _craftItem);
        pathValueMap.put(CONFIG_SECTION_RETURN_ITEM, _returnItem);

        ConfigurationSection _configSection = CONFIG.getConfigurationSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_GENERAL);
        if (_configSection == null) _configSection = CONFIG.createSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_GENERAL);
        if (_recipeType.equals("general")) {
            ReturnableItemManager.getManager().registerGeneralReturnable(_sectionName, _craftItem, _returnItem);
        }
        if (_recipeType.equals("nbt")) {
            _configSection = CONFIG.getConfigurationSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_NBT);
            if (_configSection == null) _configSection = CONFIG.createSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_NBT);
            ReturnableItemManager.getManager().registerNBTReturnable(_sectionName, _craftItem, _returnItem);
        }

        _configSection.createSection(_sectionName, pathValueMap).set("addedBy", _whoAdded);
        ItemReturnOnCraft.getMain().saveConfig();
    }

    public void removeRecipeFromConfig(String _recipeType, String _recipeName) {
        ConfigurationSection configSection;
        if (_recipeType.equals("general")) {
            configSection = CONFIG.getConfigurationSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_GENERAL);
            ReturnableItemManager.getManager().removeGeneralReturnable(_recipeName);
        }
        else {
            configSection = CONFIG.getConfigurationSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_NBT);
            ReturnableItemManager.getManager().removeNBTReturnable(_recipeName);
        }

        configSection.set(_recipeName, null);
        ItemReturnOnCraft.getMain().saveConfig();
    }


    public void configStartUp() {
        loadConfig();
    }

    public void loadConfig() {
        ReturnableItemManager.getManager().clearRegisteredItems();
        int recipeCount = 0;

        ConfigurationSection sectionGeneral = CONFIG.getConfigurationSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_GENERAL);
        ConfigurationSection sectionNBT = CONFIG.getConfigurationSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_NBT);

        if (sectionNBT != null) {
            Set<String> recipeNBTKeys = sectionNBT.getKeys(false);
            for (String recipeNBT : recipeNBTKeys) {
                ItemStack craftItem = sectionNBT.getConfigurationSection(recipeNBT).getItemStack(CONFIG_SECTION_CRAFT_ITEM);
                ItemStack returnItem = sectionNBT.getConfigurationSection(recipeNBT).getItemStack(CONFIG_SECTION_RETURN_ITEM);
                if (craftItem == null || returnItem == null) {
                    ItemReturnOnCraft.getMain().getLogger().log(Level.WARNING, "Couldn't load NBT recipe: " + recipeNBT);
                } else {
                    ReturnableItemManager.getManager().registerNBTReturnable(recipeNBT, craftItem, returnItem);
                    recipeCount++;
                }
            }
        }

        if (sectionGeneral != null) {
            Set<String> recipeGeneralKeys = sectionGeneral.getKeys(false);
            for (String recipeGeneral : recipeGeneralKeys) {
                ItemStack craftItem = sectionGeneral.getConfigurationSection(recipeGeneral).getItemStack(CONFIG_SECTION_CRAFT_ITEM);
                ItemStack returnItem = sectionGeneral.getConfigurationSection(recipeGeneral).getItemStack(CONFIG_SECTION_RETURN_ITEM);
                if (craftItem == null || returnItem == null) {
                    ItemReturnOnCraft.getMain().getLogger().log(Level.WARNING, "Couldn't load general recipe: " + recipeGeneral);
                } else {
                    ReturnableItemManager.getManager().registerGeneralReturnable(recipeGeneral, craftItem, returnItem);
                    recipeCount++;
                }
            }
        }
        if (recipeCount == 0) ItemReturnOnCraft.getMain().getLogger().log(Level.INFO, "No recipes found");
        else ItemReturnOnCraft.getMain().getLogger().log(Level.INFO, "Loaded " + recipeCount + " recipes");
    }
}
