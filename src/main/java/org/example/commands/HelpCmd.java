package org.example.commands;

import org.example.enums.CommandEnum;

public class HelpCmd implements Command<Void> {

    @Override
    public Void execute(String... args) {

        for (CommandEnum commandEnum : CommandEnum.values()) {
            System.out.println(commandEnum.getDescription());
        }

        return null;
    }
}
