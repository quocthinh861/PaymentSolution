package org.example;

import jdk.jfr.Description;
import org.example.commands.PayCmd;
import org.example.repositories.daos.BillDAO;
import org.example.repositories.daos.CustomerDAO;
import org.example.repositories.daos.PaymentDAO;
import org.example.models.Bill;
import org.example.models.Customer;
import org.example.models.Payment;
import org.example.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.verify;

public class PaymentSolutionTest extends DatabaseConnectionTest {

    CustomerDAO customerDAO;

    PaymentService paymentService;

    BillDAO billDAO;

    PaymentDAO paymentDAO;

    @BeforeEach
    public void beforeEach() throws SQLException, IOException {
        super.init();
        customerDAO = new CustomerDAO();
        billDAO = new BillDAO();
        paymentDAO = new PaymentDAO();
    }

    @Test
    @Description("Test paying multiple bills")
    public void testPaymentWithMultipleBills() throws SQLException {
        String billIds = "1,2,3";
        PayCmd payCmd = new PayCmd(new PaymentService(billDAO, paymentDAO));
        payCmd.execute(billIds);

        List<Bill> bills = billDAO.getBillsByIds(connection, List.of(1, 2, 3));
        for (Bill bill : bills) {
            assert(bill.getState().equals("PAID"));
        }

        Customer customer = customerDAO.getCustomerInfo();
        assert(customer.getBalance() == 400.0);

        List<Payment> payments = paymentDAO.listPayments();
        assert(payments.size() == 3);
    }

    @Test
    @Description("Test paying a single bill")
    public void testPaymentWithSingleBill() throws SQLException {
        String billIds = "1";
        PayCmd payCmd = new PayCmd(new PaymentService(billDAO, paymentDAO));
        payCmd.execute(billIds);

        List<Bill> bills = billDAO.getBillsByIds(connection
                , List.of(1));
        for (Bill bill : bills) {
            assert (bill.getState().equals("PAID"));
        }

        Customer customer = customerDAO.getCustomerInfo();
        assert (customer.getBalance() == 900.0);

        List<Payment> payments = paymentDAO.listPayments();

        assert (payments.size() == 1);
    }

    @Test
    @Description("Test paying a bill with insufficient funds")
    public void testPaymentWithInsufficientFunds() throws SQLException {
        String billIds = "4";
        PayCmd payCmd = new PayCmd(new PaymentService(billDAO, paymentDAO));
        payCmd.execute(billIds);

        List<Bill> bills = billDAO.getBillsByIds(connection
                , List.of(4));
        for (Bill bill : bills) {
            assert (bill.getState().equals("NOT_PAID"));
        }

        Customer customer = customerDAO.getCustomerInfo();
        assert (customer.getBalance() == 1000.0);

        List<Payment> payments = paymentDAO.listPayments();
        assert (payments.size() == 0);
    }
}
