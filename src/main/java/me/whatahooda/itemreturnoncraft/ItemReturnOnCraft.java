package me.whatahooda.itemreturnoncraft;

import me.whatahooda.itemreturnoncraft.commands.CommandAddRecipe;
import me.whatahooda.itemreturnoncraft.commands.CommandGetRecipeItems;
import me.whatahooda.itemreturnoncraft.commands.CommandRemoveRecipe;
import me.whatahooda.itemreturnoncraft.commands.tabcomplete.TabCompleteAddRecipe;
import me.whatahooda.itemreturnoncraft.commands.tabcomplete.TabCompleteGetRecipeItems;
import me.whatahooda.itemreturnoncraft.commands.tabcomplete.TabCompleteRemoveRecipe;
import me.whatahooda.itemreturnoncraft.listeners.ListenerCraftItemEvent;

import me.whatahooda.itemreturnoncraft.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemReturnOnCraft extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        ConfigManager.getManager().configStartUp();

        // Plugin startup logic
        registerClasses();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerClasses() {
        getServer().getPluginManager().registerEvents(new ListenerCraftItemEvent(), this);

        getCommand("addRecipe").setExecutor(new CommandAddRecipe());
        getCommand("addRecipe").setTabCompleter(new TabCompleteAddRecipe());

        getCommand("removeRecipe").setExecutor(new CommandRemoveRecipe());
        getCommand("removeRecipe").setTabCompleter(new TabCompleteRemoveRecipe());

        getCommand("getRecipe").setExecutor(new CommandGetRecipeItems());
        getCommand("getRecipe").setTabCompleter(new TabCompleteGetRecipeItems());

        // Doesn't load manually entered data, I gotta figure out how to do that -Whatahooda
        //getCommand("reloadConfig").setExecutor(new CommandReloadConfig());
    }
}
