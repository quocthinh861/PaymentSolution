package org.example.services;

import org.example.repositories.daos.PaymentDAO;
import org.example.factories.ConnectionFactory;
import org.example.repositories.daos.BillDAO;
import org.example.repositories.daos.CustomerDAO;
import org.example.models.Bill;
import org.example.models.Payment;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentService {

    private final BillDAO billDAO;
    private final PaymentDAO paymentDAO;

    public PaymentService(BillDAO billDAO, PaymentDAO paymentDAO) {
        this.billDAO = billDAO;
        this.paymentDAO = paymentDAO;
    }

    public void processPayment(int id, double amount, Connection conn) throws SQLException {
        billDAO.updateBillState(id, conn);
        billDAO.updateCustomerBalance(amount, conn);
        billDAO.addPayment(id, amount, conn);
    }

    public void payBill(int id, Connection conn) throws SQLException {
        Bill bill = billDAO.getBill(id, conn);
        if (bill == null) {
            System.out.println("Bill does not exist. Please try again.");
            return;
        }

        if (!"NOT_PAID".equals(bill.getState())) {
            System.out.println("Bill is already paid.");
            return;
        }

        double balance = billDAO.getBalance(conn);
        if (balance < bill.getAmount()) {
            System.out.println("Sorry! Not enough funds to proceed with payment.");
            return;
        }

        conn.setAutoCommit(false);

        try {
            processPayment(id, bill.getAmount(), conn);
            conn.commit();
            System.out.println("Payment has been completed for Bill with id " + id);
        } catch (SQLException se) {
            System.out.println("Rolling back transaction...");
            conn.rollback();
            throw se;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public void payBills(String billIds) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            List<Integer> billIdList = Arrays.stream(billIds.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            List<Bill> bills = billDAO.getBillsByIds(conn, billIdList);

            for (Bill bill : bills) {
                if (bill.getAmount() <= 0 || !"NOT_PAID".equals(bill.getState())) {
                    System.out.println("Bill with ID " + bill.getId() + " does not exist or is already paid.");
                    continue;
                }
                payBill(bill.getId(), conn);
            }

            conn.commit();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
    public List<Payment> listPayments() {
        return paymentDAO.listPayments();
    }
}
