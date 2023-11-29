package me.whatahooda.itemreturnoncraft.config;

import me.whatahooda.itemreturnoncraft.ItemReturnOnCraft;
import me.whatahooda.itemreturnoncraft.models.ReturnableItemManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Level;

public class ConfigManager {

    private static final ItemReturnOnCraft PLUGIN_INSTANCE;

    static {
        PLUGIN_INSTANCE = (ItemReturnOnCraft) Bukkit.getServer().getPluginManager().getPlugin("CraftReturn");
    }
    private static ConfigManager managerInstance;
    private static final FileConfiguration CONFIG = PLUGIN_INSTANCE.getConfig();

    public static final String CONFIG_SECTION_WORKBENCH = "returns-workbench";
    public static final String CONFIG_SECTION_GENERAL = "general";
    public static final String CONFIG_SECTION_NBT = "nbt";
    public static final String CONFIG_SECTION_CRAFT_ITEM = "craft-item";
    public static final String CONFIG_SECTION_RETURN_ITEM = "return-item";

    public static final ArrayList<String> SUB_COMMANDS_RECIPE_TYPE = new ArrayList<>(Arrays.asList("general", "nbt"));
    private static final ArrayList<String> recipeNamesGeneral = new ArrayList<>();
    private static final ArrayList<String> recipeNamesNBT = new ArrayList<>();


    public static ConfigManager getManager() {
        if (managerInstance == null) {
            managerInstance = new ConfigManager();
        }
        return managerInstance;
    }


    public ArrayList<String> getRecipeNamesGeneral() {
        return recipeNamesGeneral;
    }
    public ArrayList<String> getRecipeNamesNBT() {
        return recipeNamesNBT;
    }

    public void registerRecipeNameGeneral(String _toAdd) {
        if (!recipeNamesGeneral.contains(_toAdd)) recipeNamesGeneral.add(_toAdd);
    }
    public void registerRecipeNameNBT(String _toAdd) {
        if (!recipeNamesNBT.contains(_toAdd)) recipeNamesNBT.add(_toAdd);
    }

    public void removeRecipeNameGeneral(String _toRemove) {
        if (!recipeNamesGeneral.contains(_toRemove)) recipeNamesGeneral.remove(_toRemove);
    }
    public void removeRecipeNameNBT(String _toRemove) {
        if (!recipeNamesNBT.contains(_toRemove)) recipeNamesNBT.remove(_toRemove);
    }

    public void addRecipeToConfig(String _recipeType, String _sectionName, ItemStack _craftItem, ItemStack _returnItem, String _whoAdded) {
        LinkedHashMap<String, ItemStack> pathValueMap = new LinkedHashMap<>();
        pathValueMap.put(CONFIG_SECTION_CRAFT_ITEM, _craftItem);
        pathValueMap.put(CONFIG_SECTION_RETURN_ITEM, _returnItem);

        ConfigurationSection _configSection = CONFIG.getConfigurationSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_GENERAL);
        if (_configSection == null) _configSection = CONFIG.createSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_GENERAL);
        if (_recipeType.equals("general")) {
            registerRecipeNameGeneral(_sectionName);
            ReturnableItemManager.getManager().registerGeneralReturnable(_sectionName, _craftItem, _returnItem);
        }
        if (_recipeType.equals("nbt")) {
            _configSection = CONFIG.getConfigurationSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_NBT);
            if (_configSection == null) _configSection = CONFIG.createSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_NBT);
            registerRecipeNameNBT(_sectionName);
            ReturnableItemManager.getManager().registerNBTReturnable(_sectionName, _craftItem, _returnItem);
        }

        // Add to config and save
        _configSection.createSection(_sectionName, pathValueMap).set("addedBy", _whoAdded);
        PLUGIN_INSTANCE.saveConfig();
    }

    public void removeRecipeFromConfig(String _recipeType, String _recipeName) {
        ConfigurationSection configSection;
        if (_recipeType.equals("general")) {
            configSection = CONFIG.getConfigurationSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_GENERAL);
            removeRecipeNameGeneral(_recipeName);
            //ReturnableItemManager.getManager().removeGeneralReturnable(configSection.getItemStack(_recipeName + "." + CONFIG_SECTION_CRAFT_ITEM).getType());
            ReturnableItemManager.getManager().removeGeneralReturnable(_recipeName);
        }
        else {
            configSection = CONFIG.getConfigurationSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_NBT);
            removeRecipeNameNBT(_recipeName);
            //ReturnableItemManager.getManager().removeNBTReturnable(configSection.getItemStack(_recipeName + "." + CONFIG_SECTION_CRAFT_ITEM));
            ReturnableItemManager.getManager().removeNBTReturnable(_recipeName);
        }

        configSection.set(_recipeName, null);
        PLUGIN_INSTANCE.saveConfig();
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
                    PLUGIN_INSTANCE.getLogger().log(Level.WARNING, "Couldn't load NBT recipe: " + recipeNBT);
                } else {
                    ReturnableItemManager.getManager().registerNBTReturnable(recipeNBT, craftItem, returnItem);
                    registerRecipeNameNBT(recipeNBT);
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
                    PLUGIN_INSTANCE.getLogger().log(Level.WARNING, "Couldn't load general recipe: " + recipeGeneral);
                } else {
                    ReturnableItemManager.getManager().registerGeneralReturnable(recipeGeneral, craftItem, returnItem);
                    registerRecipeNameGeneral(recipeGeneral);
                    recipeCount++;
                }
            }
        }
        if (recipeCount == 0) PLUGIN_INSTANCE.getLogger().log(Level.INFO, "No recipes found");
        else PLUGIN_INSTANCE.getLogger().log(Level.INFO, "Successfully loaded " + recipeCount + " recipes");
    }
}
