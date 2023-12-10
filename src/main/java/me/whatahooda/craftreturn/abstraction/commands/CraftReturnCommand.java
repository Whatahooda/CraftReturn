package me.whatahooda.craftreturn.abstraction.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface CraftReturnCommand {
    String name = null;
    String description = null;
    String commandFormat = null;
    String permissionNode = null;

    String getName();
    String getDescription();
    String getCommandFormat();
    String getPermissionNode();

    boolean executeCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
}
