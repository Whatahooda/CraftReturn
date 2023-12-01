package me.whatahooda.itemreturnoncraft.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public class CraftReturnUtil {
    private static final NamedTextColor INFO_COLOR = NamedTextColor.WHITE;
    private static final NamedTextColor ERROR_COLOR = NamedTextColor.RED;

    public static final String CRAFT_RETURN_TAG = "[CraftReturn] ";
    public static final TextComponent COMMAND_NO_PERMISSION = Component.text(CRAFT_RETURN_TAG)
            .color(ERROR_COLOR)
            .append(Component.text("You do not have permission to run that command"));

    public static TextComponent messageInfo(String infoMessage) {
        return Component.text(CRAFT_RETURN_TAG)
                .color(INFO_COLOR)
                .append(Component.text(infoMessage));
    }

    public static TextComponent messageError(String errorMessage) {
        return Component.text(CRAFT_RETURN_TAG)
                .color(ERROR_COLOR)
                .append(Component.text(errorMessage));
    }
}
