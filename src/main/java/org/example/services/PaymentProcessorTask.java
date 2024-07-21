package org.example.services;

import org.example.factories.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimerTask;

public class PaymentProcessorTask extends TimerTask {
    private final PaymentService paymentService;
    private final int billId;

    public PaymentProcessorTask(PaymentService paymentService, int billId) {
        this.paymentService = paymentService;
        this.billId = billId;
    }

    @Override
    public void run() {
        try (Connection conn = ConnectionFactory.getConnection()) {
            paymentService.payBill(billId, conn);
        } catch (SQLException e) {
            System.out.println("An error occurred while processing payment for bill ID " + billId);
            e.printStackTrace();
        } finally {
            System.out.println("Payment processing completed for bill ID " + billId);
        }
    }
}
