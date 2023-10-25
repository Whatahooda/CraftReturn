package me.whatahooda.itemreturnoncraft;

import org.bukkit.plugin.java.JavaPlugin;

public final class ItemReturnOnCraft extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new RecipeReturnsBottle(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
