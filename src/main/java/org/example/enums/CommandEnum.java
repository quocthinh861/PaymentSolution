package org.example.enums;

import org.example.commands.*;

public enum CommandEnum {
    PAY("pay"),
    LIST_BILL("list_bill"),
    DUE_DATE("due_date"),
    ADD_FUND("add"),
    EXIT("exit"),
    LIST_PAYMENT("list_payment");

    private final String command;

    CommandEnum(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static CommandEnum fromString(String command) {
        for (CommandEnum commandEnum : CommandEnum.values()) {
            if (commandEnum.getCommand().equals(command)) {
                return commandEnum;
            }
        }
        return null;
    }
}
