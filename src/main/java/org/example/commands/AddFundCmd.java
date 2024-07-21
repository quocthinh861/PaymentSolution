package org.example.commands;

import org.example.repositories.daos.CustomerDAO;

public class AddFundCmd implements Command<Void> {

    private CustomerDAO customerDAO;

    public AddFundCmd(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public boolean validate(String... args) {
        if (args.length < 1) {
            System.out.println("Usage: add <amount>");
            return false;
        }

        try {
            Double amount = Double.parseDouble(args[0]);
            if(amount <= 0) {
                System.out.println("Amount must be greater than 0");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Amount must be a number");
            return false;
        }

        return true;
    }

    @Override
    public Void execute(String... args) {
        if(!validate(args)) {
            return null;
        }
        double amount = Double.parseDouble(args[0]);
        customerDAO.addFund(amount);
        return null;
    }

}
