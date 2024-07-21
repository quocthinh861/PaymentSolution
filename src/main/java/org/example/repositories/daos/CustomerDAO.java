package org.example.repositories.daos;

import org.example.UserContext;
import org.example.factories.ConnectionFactory;
import org.example.models.Customer;
import org.example.repositories.CustomerRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements CustomerRepository {

    @Override
    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("id"),
                        rs.getDouble("balance")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong. Please try again.");
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public Customer getCustomerInfo() {
        Customer customer = null;
        String sql = "SELECT TOP 1 * FROM customers WHERE id = ?";
        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, UserContext.getInstance().getUserId());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        customer = new Customer(
                                rs.getInt("id"),
                                rs.getDouble("balance")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong. Please try again.");
        }
        return customer;
    }

    @Override
    public void addFund(double amount) {
        String sql = "UPDATE customers SET balance = balance + ? WHERE id = ?";
        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setDouble(1, amount);
                pstmt.setInt(2, UserContext.getInstance().getUserId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong. Please try again.");
        }
    }
}
