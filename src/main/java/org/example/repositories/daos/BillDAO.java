package org.example.repositories.daos;

import org.example.UserContext;
import org.example.models.Bill;
import org.example.factories.ConnectionFactory;
import org.example.repositories.BillRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BillDAO implements BillRepository {
    @Override
    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();

        String sql = "SELECT * FROM bills WHERE customer_id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, UserContext.getInstance().getUserId());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bills.add(
                            new Bill.Builder()
                                    .setId(rs.getInt("id"))
                                    .setType(rs.getString("type"))
                                    .setAmount(rs.getDouble("amount"))
                                    .setDueDate(rs.getString("due_date"))
                                    .setState(rs.getString("state"))
                                    .setProvider(rs.getString("provider"))
                                    .build()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public List<Bill> getOverDueBills() {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills WHERE state = 'NOT_PAID' AND due_date < CURRENT_DATE";
        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bills.add(new Bill(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("due_date"),
                        rs.getString("state"),
                        rs.getString("provider")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public Bill getBill(int id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM bills WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double amount = rs.getDouble("amount");
                    String state = rs.getString("state");
                    return new Bill.Builder()
                            .setId(rs.getInt("id"))
                            .setType(rs.getString("type"))
                            .setAmount(amount)
                            .setDueDate(rs.getString("due_date"))
                            .setState(state)
                            .setProvider(rs.getString("provider"))
                            .build();
                }
            }
        }
        return null;
    }

    @Override
    public void updateBillState(int id, Connection conn) throws SQLException {
        String sql = "UPDATE bills SET state = 'PAID' WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateCustomerBalance(int billId, Connection conn) throws SQLException {
        String sql = "UPDATE customers SET balance = balance - " +
                "(SELECT amount FROM bills WHERE id = ? AND customer_id = ?) " +
                "WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, billId);
            stmt.setInt(2, UserContext.getInstance().getUserId());
            stmt.setInt(3, UserContext.getInstance().getUserId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Bill not found or balance update failed.");
            }
        }
    }

    @Override
    public void addPayment(int id, Connection conn) throws SQLException {
        String sql = "INSERT INTO payments (bill_id, customer_id, payment_date) VALUES (?, ?, CURRENT_DATE())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, UserContext.getInstance().getUserId());
            stmt.executeUpdate();
        }
    }

    @Override
    public double getBalance(Connection conn) throws SQLException {
        String sql = "SELECT balance FROM customers WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, UserContext.getInstance().getUserId());
        try (stmt; ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        }
        return 0;
    }

    @Override
    public List<Bill> getBillsByIds(Connection conn, List<Integer> billIdList) throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String questionMarks = billIdList.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        String sql = "SELECT * FROM bills WHERE id IN (" + questionMarks + ") AND customer_id = ? ORDER BY due_date ASC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < billIdList.size(); i++) {
                stmt.setInt(i + 1, billIdList.get(i));
            }
            stmt.setInt(billIdList.size() + 1, UserContext.getInstance().getUserId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bills.add(new Bill(
                            rs.getInt("id"),
                            rs.getString("type"),
                            rs.getDouble("amount"),
                            rs.getString("due_date"),
                            rs.getString("state"),
                            rs.getString("provider")
                    ));
                }
            }
        }
        return bills;
    }

    @Override
    public double getBillAmount(int billId, Connection conn) throws SQLException {
        String sql = "SELECT amount FROM bills WHERE id = ? AND customer_id = ? AND state = 'NOT_PAID'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, billId);
            stmt.setInt(2, UserContext.getInstance().getUserId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("amount");
                }
            }
        }
        return 0;
    }
}
