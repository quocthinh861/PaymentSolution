package org.example.repositories;

import org.example.models.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BillRepository {
    List<Bill> getAllBills();
    List<Bill> getOverDueBills();
    Bill getBill(int id, Connection conn) throws SQLException;
    void updateBillState(int id, Connection conn) throws SQLException;
    void updateCustomerBalance(double amount, Connection conn) throws SQLException;
    void addPayment(int id, double amount, Connection conn) throws SQLException;
    double getBalance(Connection conn) throws SQLException;
    List<Bill> getBillsByIds(Connection conn, List<Integer> billIdList) throws SQLException;
    double getBillAmount(int billId, Connection conn) throws SQLException;
}