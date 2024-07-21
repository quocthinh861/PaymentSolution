package org.example.commands;

import java.util.Scanner;

public class ExitCmd implements Command<Boolean> {

    @Override
    public Boolean execute(String... args) {
        System.out.print("Are you sure you want to exit the Payment System? (yes/no): ");
        Scanner scanner = new Scanner(System.in);
        String confirm = scanner.nextLine();
        return confirm.equalsIgnoreCase("yes");
    }
}
