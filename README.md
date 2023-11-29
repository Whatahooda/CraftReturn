# CraftReturn
A Minecraft plugin which allows you to add returnable items when crafting. This is per item, and not per recipe. For example, the default recipes give back empty bottles when a player crafts any items which need potions.
This plugin replicates the properties of filled buckets in crafting recipes. Such as cake, the milk bucket is emptied and returned to the player.

## Commands
### /addRecipe recipeType recipeName\n
Adds a new item that is returned on craft. Uses the items in your main-hand(craft-item) and off-hand(return-item)

### /removeRecipe recipeType recipeName confirm
Removes the specified recipe from the config

### /getRecipe recipeType recipeName confirm
Add the items involved in a recipe to your main-hand(craft-item) and off-hand(return-item), this will replace those slots
