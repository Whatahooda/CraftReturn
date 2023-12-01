# CraftReturn

A Minecraft plugin which allows you to add returnable items when crafting. This is per item, and not per recipe. For example, the default recipes give back empty bottles when a player crafts any items which need potions.
This plugin replicates the properties of filled buckets in crafting recipes. Such as cake, the milk bucket is emptied and returned to the player.


## Commands and Permissions

#### /addRecipe recipeType recipeName
**Permission node**: craft-return.commands.add-recipe
Adds a new item that is returned on craft. Uses the items in your main-hand(craft-item) and off-hand(return-item)
When a player adds a recipe with this command, their name will be registered with the recipe in the config for security purposes.

#### /removeRecipe recipeType recipeName confirm
**Permission node**: craft-return.commands.remove-recipe
Removes the specified recipe from the config


#### /getRecipe recipeType recipeName confirm
**Permission node**: craft-return.commands.get-recipe
Add the items involved in a recipe to your main-hand(craft-item) and off-hand(return-item), this will replace those slots


#### /reloadConfig
**Permission node**: craft-return.commands.reload-config
Loads and reads the locally stored config file
Note: The loaded recipe count displayed on the command line may be innacurate, but all valid recipes will be properly loaded
