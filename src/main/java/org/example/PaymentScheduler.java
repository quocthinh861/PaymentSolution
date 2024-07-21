package org.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PaymentScheduler {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void schedulePayment(Runnable paymentTask, long delay, TimeUnit unit) {
        scheduler.schedule(paymentTask, delay, unit);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
