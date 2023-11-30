package me.whatahooda.itemreturnoncraft.config;

import me.whatahooda.itemreturnoncraft.ItemReturnOnCraft;
import me.whatahooda.itemreturnoncraft.models.ReturnableItemManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Level;

public class ConfigManager {

    private static ConfigManager managerInstace;
    private static final FileConfiguration CONFIG = ItemReturnOnCraft.getMain().getConfig();

    public static final String CONFIG_SECTION_WORKBENCH = "returns-workbench";
    public static final String CONFIG_SECTION_GENERAL = "general";
    public static final String CONFIG_SECTION_NBT = "nbt";
    public static final String CONFIG_SECTION_CRAFT_ITEM = "craft-item";
    public static final String CONFIG_SECTION_RETURN_ITEM = "return-item";

    public static final ArrayList<String> SUB_COMMANDS_RECIPE_TYPE = new ArrayList<>(Arrays.asList("general", "nbt"));


    public static ConfigManager getManager() {
        if (managerInstace == null) {
            managerInstace = new ConfigManager();
        }
        return managerInstace;
    }

    public void addRecipeToConfig(String recipeType, String sectionName, ItemStack craftItem, ItemStack returnItem, String whoAdded) {
        LinkedHashMap<String, ItemStack> pathValueMap = new LinkedHashMap<>();
        pathValueMap.put(CONFIG_SECTION_CRAFT_ITEM, craftItem);
        pathValueMap.put(CONFIG_SECTION_RETURN_ITEM, returnItem);

        ConfigurationSection configSection = CONFIG.getConfigurationSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_GENERAL);
        if (configSection == null) configSection = CONFIG.createSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_GENERAL);
        if (recipeType.equals("general")) {
            ReturnableItemManager.getManager().registerGeneralReturnable(sectionName, craftItem, returnItem);
        }
        if (recipeType.equals("nbt")) {
            configSection = CONFIG.getConfigurationSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_NBT);
            if (configSection == null) configSection = CONFIG.createSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_NBT);
            ReturnableItemManager.getManager().registerNBTReturnable(sectionName, craftItem, returnItem);
        }

        configSection.createSection(sectionName, pathValueMap).set("addedBy", whoAdded);
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
