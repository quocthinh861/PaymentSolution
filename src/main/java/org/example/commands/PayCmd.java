package org.example.commands;

import org.example.services.PaymentService;

public class PayCmd implements Command<Void> {

    PaymentService paymentService;

    public PayCmd(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public Void execute(String... args) {
        String billIds = String.join(",", args);
        paymentService.payBills(billIds);
        return null;
    }
}
