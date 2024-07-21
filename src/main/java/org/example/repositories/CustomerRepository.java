package org.example.repositories;

import org.example.models.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerRepository {
    List<Customer> getCustomers();
    Customer getCustomerInfo() throws SQLException;
    void addFund(double amount) throws SQLException;
}