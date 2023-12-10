package me.whatahooda.craftreturn.abstraction.commands;

import lombok.Getter;

import java.util.HashMap;

public class CommandBranch {

    @Getter
    private CraftReturnCommand command;

    @Getter
    private HashMap<String, CommandBranch> subBranches;

    public CommandBranch(CraftReturnCommand newCommand) {
        command = newCommand;
        subBranches = new HashMap<>();
    }
}
