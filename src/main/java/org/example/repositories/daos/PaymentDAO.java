package org.example.repositories.daos;

import org.example.UserContext;
import org.example.factories.ConnectionFactory;
import org.example.models.Payment;
import org.example.repositories.PaymentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO implements PaymentRepository {

    @Override
    public List<Payment> listPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT p.*, b.state, b.amount FROM payments p " +
                "JOIN bills b ON p.bill_id = b.id WHERE p.customer_id = ?";
        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, UserContext.getInstance().getUserId());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Payment payment = new Payment.Builder()
                                .id(rs.getInt("id"))
                                .amount(rs.getDouble("amount"))
                                .billId(rs.getInt("bill_id"))
                                .customerId(rs.getInt("customer_id"))
                                .state(rs.getString("state"))
                                .paymentDate(rs.getString("payment_date"))
                                .build();
                        payments.add(payment);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong. Please try again.");
            e.printStackTrace();
        }
        return payments;
    }

    @Override
    public void addScheduledPayment(int billId, Date scheduleTime) throws SQLException {
        try(Connection conn = ConnectionFactory.getConnection()) {
            String sql = "INSERT INTO scheduled_payments (customer_id, bill_id, schedule_date) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, UserContext.getInstance().getUserId());
                stmt.setInt(2, billId);
                stmt.setDate(3, scheduleTime);
                stmt.executeUpdate();
            }
        }
    }
}
