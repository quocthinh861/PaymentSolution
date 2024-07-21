package org.example.commands;

import org.example.services.PaymentService;

public class PayCmd implements Command<Void> {

    PaymentService paymentService;

    public PayCmd(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public boolean validate(String... args) {
        if (args.length == 0) {
            System.out.println("Please provide bill ids to pay");
            return false;
        }

        for (String arg : args) {
            try {
                Integer.parseInt(arg);
            } catch (NumberFormatException e) {
                System.out.println("Invalid bill id: " + arg);
                return false;
            }
        }

        return true;
    }

    @Override
    public Void execute(String... args) {
        if (!validate(args)) {
            return null;
        }
        String billIds = String.join(",", args);
        paymentService.payBills(billIds);
        return null;
    }
}
