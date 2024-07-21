package org.example.models;

public class Payment {

    private int id;
    private double amount;
    private String paymentDate;
    private int customerId;
    private int billId;
    private String state;

    private Payment(Builder builder) {
        this.id = builder.id;
        this.amount = builder.amount;
        this.paymentDate = builder.paymentDate;
        this.customerId = builder.customerId;
        this.billId = builder.billId;
        this.state = builder.state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static class Builder {
        private int id;
        private double amount;
        private String paymentDate;
        private int customerId;
        private int billId;
        private String state;

        public Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder paymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
            return this;
        }

        public Builder customerId(int customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder billId(int billId) {
            this.billId = billId;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }
}
