package org.example.commands;

import org.example.repositories.daos.CustomerDAO;

public class AddFundCmd implements Command<Void> {

    private CustomerDAO customerDAO;

    public AddFundCmd(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public Void execute(String... args) {
        double amount = Double.parseDouble(args[0]);
        customerDAO.addFund(amount);
        return null;
    }

}
