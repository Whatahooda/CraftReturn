package me.whatahooda.craftreturn;

import lombok.Getter;
import me.whatahooda.craftreturn.commands.CommandAddRecipe;
import me.whatahooda.craftreturn.commands.CommandGetRecipeItems;
import me.whatahooda.craftreturn.commands.CommandReloadConfig;
import me.whatahooda.craftreturn.commands.CommandRemoveRecipe;
import me.whatahooda.craftreturn.commands.tabcomplete.TabCompleteAddRecipe;
import me.whatahooda.craftreturn.commands.tabcomplete.TabCompleteGetRecipeItems;
import me.whatahooda.craftreturn.commands.tabcomplete.TabCompleteRemoveRecipe;
import me.whatahooda.craftreturn.listeners.ListenerCraftItemEvent;

import me.whatahooda.craftreturn.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftReturn extends JavaPlugin {

    @Getter
    private static CraftReturn main;

    @Override
    public void onEnable() {
        main = this;

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

        getCommand("reloadConfig").setExecutor(new CommandReloadConfig());
    }
}
