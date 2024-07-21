package org.example.commands;

import org.example.models.Payment;
import org.example.services.PaymentService;
import org.example.utils.ConsoleUtils;

import java.util.List;

public class ListPaymentCmd implements Command<Void> {

    PaymentService paymentService;

    public ListPaymentCmd(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public Void execute(String... args) {
        List<Payment> payments = paymentService.listPayments();
        ConsoleUtils.showPayments(payments);
        return null;
    }
}
