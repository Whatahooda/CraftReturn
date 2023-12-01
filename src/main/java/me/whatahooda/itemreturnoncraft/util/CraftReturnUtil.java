package me.whatahooda.itemreturnoncraft.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

public class CraftReturnUtil {
    private static final int INFO_COLOR = 0xFFFFFF;
    private static final int ERROR_COLOR = 0xAA0000;

    public static final String CRAFT_RETURN_TAG = "[CraftReturn] ";
    public static final TextComponent COMMAND_NO_PERMISSION = Component.text(CRAFT_RETURN_TAG)
            .color(TextColor.color(ERROR_COLOR))
            .append(Component.text("You do not have permission to run that command"));

    public static TextComponent messageInfo(String infoMessage) {
        return Component.text(CRAFT_RETURN_TAG)
                .color(TextColor.color(INFO_COLOR))
                .append(Component.text(infoMessage));
    }

    public static TextComponent messageError(String errorMessage) {
        return Component.text(CRAFT_RETURN_TAG)
                .color(TextColor.color(ERROR_COLOR))
                .append(Component.text(errorMessage));
    }
}
