# CraftReturn  

A Minecraft plugin which allows you to add returnable items when crafting. This is per item, and not per recipe. For example, the default recipes give back empty bottles when a player crafts any items which need potions.  
This plugin replicates the properties of filled buckets in crafting recipes. Such as cake, the milk buckets are emptied and returned to the player.  


## Commands and Permissions  

`/addRecipe recipeType recipeName`  
**Permission node**: craftreturn.command.addrecipe  
Adds a new item that is returned on craft. Uses the items in your main-hand(craft-item) and off-hand(return-item)  
When a player adds a recipe with this command, their name will be registered with the recipe in the config for security purposes.  


`/removeRecipe recipeType recipeName confirm`  
**Permission node**: craftreturn.command.removerecipe  
Removes the specified recipe from the config  


`/getRecipe recipeType recipeName confirm`  
**Permission node**: craftreturn.command.getrecipe  
Add the items involved in a recipe to your main-hand(craft-item) and off-hand(return-item), this will replace those slots  


`/reloadConfig`  
**Permission node**: craftreturn.command.reloadconfig  
Loads and reads the locally stored config file  
