package org.example.enums;

import org.example.commands.*;

public enum CommandEnum {
    PAY("pay", "pay <amount> - Pay the bill"),
    LIST_BILL("list_bill", "list_bill - List all bills"),
    DUE_DATE("due_date", "due_date - List all due dates"),
    ADD_FUND("add", "add <amount> - Add fund to balance"),
    LIST_PAYMENT("list_payment", "list_payment - List all payments"),
    EXIT("exit", "exit - Exit the program"),
    HELP("help", "help - Display available commands"),
    SCHEDULE("schedule", "schedule <billId> <due_date> - Schedule a payment");

    private final String command;

    private final String description;

    CommandEnum(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
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
