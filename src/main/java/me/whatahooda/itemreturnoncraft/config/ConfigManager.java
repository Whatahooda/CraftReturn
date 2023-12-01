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

    public void removeRecipeFromConfig(String recipeType, String recipeName) {
        ConfigurationSection configSection;
        if (recipeType.equals("general")) {
            configSection = CONFIG.getConfigurationSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_GENERAL);
            ReturnableItemManager.getManager().removeGeneralReturnable(recipeName);
        }
        else {
            configSection = CONFIG.getConfigurationSection(CONFIG_SECTION_WORKBENCH + "." + CONFIG_SECTION_NBT);
            ReturnableItemManager.getManager().removeNBTReturnable(recipeName);
        }

        configSection.set(recipeName, null);
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
            recipeCount = registerRecipes(sectionNBT, "nbt");
        }
        if (sectionGeneral != null) {
            recipeCount = registerRecipes(sectionGeneral, "general");
        }

        if (recipeCount == 0) ItemReturnOnCraft.getMain().getLogger().log(Level.INFO, "No recipes found");
        else ItemReturnOnCraft.getMain().getLogger().log(Level.INFO, "Loaded " + recipeCount + " recipes");
    }

    private int registerRecipes(ConfigurationSection section, String type) {
        int recipeCount = 0;
        Set<String> recipeKeys = section.getKeys(false);
        for (String recipe : recipeKeys) {
            ItemStack craftItem = section.getConfigurationSection(recipe).getItemStack(CONFIG_SECTION_CRAFT_ITEM);
            ItemStack returnItem = section.getConfigurationSection(recipe).getItemStack(CONFIG_SECTION_RETURN_ITEM);
            if (craftItem == null || returnItem == null) {
                ItemReturnOnCraft.getMain().getLogger().log(Level.WARNING, "Couldn't load recipe: " + recipe);
            } else {
                if (type.equals("general")) ReturnableItemManager.getManager().registerGeneralReturnable(recipe, craftItem, returnItem);
                else if (type.equals("nbt")) ReturnableItemManager.getManager().registerNBTReturnable(recipe, craftItem, returnItem);
                recipeCount++;
            }
        }
        return recipeCount;

    }
}
