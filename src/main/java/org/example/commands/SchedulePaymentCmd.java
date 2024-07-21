package org.example.commands;

import org.example.services.PaymentProcessorTask;
import org.example.services.PaymentService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class SchedulePaymentCmd implements Command<Void> {
    private final PaymentService paymentService;

    public SchedulePaymentCmd(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public boolean validate(String... args) {
        if (args.length < 2) {
            System.out.println("Usage: schedule <billId> <yyyy-MM-dd HH:mm:ss>");
            return false;
        }

        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Bill ID must be a number");
            return false;
        }

        if (!args[1].matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            System.out.println("Date must be in the format yyyy-MM-dd HH:mm:ss");
            return false;
        }

        return true;
    }

    @Override
    public Void execute(String... args) {
        int billId;
        Date scheduleTime;
        try {
            billId = Integer.parseInt(args[0]);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            scheduleTime = dateFormat.parse(args[1]);
        } catch (Exception e) {
            System.out.println("Invalid arguments. Please provide a valid billId and schedule time.");
            return null;
        }

        schedulePayment(billId, scheduleTime);
        return null;
    }

    private void schedulePayment(int billId, Date scheduleTime) {
        Timer timer = new Timer();
        timer.schedule(new PaymentProcessorTask(paymentService, billId), scheduleTime);
        System.out.println("Payment scheduled for bill ID " + billId + " at " + scheduleTime);
    }
}
