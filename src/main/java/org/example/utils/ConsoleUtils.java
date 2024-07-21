package org.example.utils;

import org.example.models.Bill;
import org.example.models.Payment;

import java.util.List;

public class ConsoleUtils {
    public static void showBills(List<Bill> bills) {
        if(bills.isEmpty()) {
            System.out.println("No bills found.");
            return;
        }

        System.out.printf("%-10s %-10s %-10s %-12s %-10s %-15s%n", "Bill No.", "Type", "Amount", "Due Date", "State", "Provider");
        System.out.println("---------------------------------------------------------------");
        for (Bill bill : bills) {
            System.out.printf("%-10s %-10s %-10s %-12s %-10s %-15s%n", bill.getId(), bill.getType(), bill.getAmount(), bill.getDueDate(), bill.getState(), bill.getProvider());
        }
    }

    public static void showPayments(List<Payment> payments) {
        if(payments.isEmpty()) {
            System.out.println("No payments found.");
            return;
        }

        System.out.printf("%-15s %-10s %-10s %-12s %-10s%n", "Payment No.", "Bill No.", "Amount", "Payment Date", "State");
        System.out.println("---------------------------------------------------------------");
        for (Payment payment : payments) {
            System.out.printf("%-15s %-10s %-10s %-12s %-10s%n", payment.getId(), payment.getBillId(), payment.getAmount(), payment.getPaymentDate(), payment.getState());
        }
    }
}
