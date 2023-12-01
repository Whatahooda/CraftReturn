package me.whatahooda.itemreturnoncraft.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public class CraftReturnUtil {
    private static final NamedTextColor TAG_COLOR = NamedTextColor.GOLD;
    private static final NamedTextColor INFO_COLOR = NamedTextColor.WHITE;
    private static final NamedTextColor ERROR_COLOR = NamedTextColor.RED;

    public static final TextComponent CRAFT_RETURN_TAG = Component.text("[CraftReturn] ").color(TAG_COLOR);
    public static final TextComponent COMMAND_NO_PERMISSION = CRAFT_RETURN_TAG
            .append(Component.text("You do not have permission to run that command").color(ERROR_COLOR));

    public static TextComponent messageInfo(String infoMessage) {
        return CRAFT_RETURN_TAG
                .append(Component.text(infoMessage).color(INFO_COLOR));
    }

    public static TextComponent messageError(String errorMessage) {
        return CRAFT_RETURN_TAG
                .append(Component.text(errorMessage).color(ERROR_COLOR));
    }
}
