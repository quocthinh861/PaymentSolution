package org.example.models;

public class Bill {

    private int id;
    private int customerId;
    private String type;
    private double amount;
    private String dueDate;
    private String state;
    private String provider;

    // Constructors, getters, and setters

    public Bill(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.amount = builder.amount;
        this.dueDate = builder.dueDate;
        this.state = builder.state;
        this.provider = builder.provider;
        this.customerId = builder.customerId;
    }

    public Bill(int id, String type, double amount, String dueDate, String state, String provider) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.state = state;
        this.provider = provider;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public static class Builder {
        private int id;
        private String type;
        private double amount;
        private String dueDate;
        private String state;
        private String provider;
        private int customerId;

        public Builder() {
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder setDueDate(String dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        public Builder setProvider(String provider) {
            this.provider = provider;
            return this;
        }

        public Builder setCustomerId(int customerId) {
            this.customerId = customerId;
            return this;
        }

        public Bill build() {
            return new Bill(id, type, amount, dueDate, state, provider);
        }
    }
}
