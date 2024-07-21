package org.example;

import jdk.jfr.Description;
import org.example.repositories.daos.BillDAO;
import org.example.repositories.daos.CustomerDAO;
import org.example.repositories.daos.PaymentDAO;
import org.example.models.Bill;
import org.example.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class MockPaymentTest {

    @Mock
    private CustomerDAO customerDAO;

    @Mock
    private BillDAO billDAO;

    @Mock
    private PaymentDAO paymentDAO;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        paymentService = new PaymentService(billDAO, paymentDAO);
    }

    @Test
    @Description("Test paying bills when funds are sufficient")
    public void testPayBills() throws SQLException {
        List<Bill> bills = Arrays.asList(
                new Bill(1, "Utility", 100.0, "2024-07-21", "NOT_PAID", "Provider A"),
                new Bill(2, "Internet", 50.0, "2024-07-21", "NOT_PAID", "Provider B")
        );

        when(billDAO.getBillsByIds(any(Connection.class), anyList())).thenReturn(bills);
        when(billDAO.getBalance(any(Connection.class))).thenReturn(150.0);
        doAnswer(new Answer<Bill>() {
            @Override
            public Bill answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                int id = (int) args[0];
                return bills.stream().filter(bill -> bill.getId() == id).findFirst().orElse(null);
            }

        }).when(billDAO).getBill(anyInt(), any(Connection.class));
        doNothing().when(billDAO).updateBillState(anyInt(), any(Connection.class));
        doNothing().when(billDAO).updateCustomerBalance(anyDouble(), any(Connection.class));
        doNothing().when(billDAO).addPayment(anyInt(), anyDouble(), any(Connection.class));

        paymentService.payBills("1,2");

        verify(billDAO, times(2)).updateBillState(anyInt(), any(Connection.class));
        verify(billDAO, times(2)).updateCustomerBalance(anyDouble(), any(Connection.class));
        verify(billDAO, times(2)).addPayment(anyInt(), anyDouble(), any(Connection.class));
    }

    @Test
    @Description("Test paying bills when funds are insufficient")
    public void testPayBillsWithInsufficientFunds() throws SQLException {
        List<Bill> bills = Arrays.asList(
                new Bill(1, "Utility", 100.0, "2024-07-21", "NOT_PAID", "Provider A"),
                new Bill(2, "Internet", 150.0, "2024-07-21", "NOT_PAID", "Provider B")
        );

        when(billDAO.getBillsByIds(any(Connection.class), anyList())).thenReturn(bills);
        when(billDAO.getBalance(any(Connection.class))).thenReturn(100.0);
        doAnswer(new Answer<Bill>() {
            @Override
            public Bill answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                int id = (int) args[0];
                return bills.stream().filter(bill -> bill.getId() == id).findFirst().orElse(null);
            }

        }).when(billDAO).getBill(anyInt(), any(Connection.class));

        paymentService.payBills("1,2");

        verify(billDAO, times(1)).updateBillState(anyInt(), any(Connection.class));
        verify(billDAO, times(1)).updateCustomerBalance(anyDouble(), any(Connection.class));
        verify(billDAO, times(1)).addPayment(anyInt(), anyDouble(), any(Connection.class));
    }

    @Test
    @Description("Test attempting to pay a bill that is already paid")
    public void testPayBillAlreadyPaid() throws SQLException {
        List<Bill> bills = Collections.singletonList(
                new Bill(1, "Utility", 100.0, "2024-07-21", "PAID", "Provider A")
        );

        when(billDAO.getBillsByIds(any(Connection.class), anyList())).thenReturn(bills);

        paymentService.payBills("1");

        verify(billDAO, times(0)).updateBillState(anyInt(), any(Connection.class));
        verify(billDAO, times(0)).updateCustomerBalance(anyDouble(), any(Connection.class));
        verify(billDAO, times(0)).addPayment(anyInt(), anyDouble(), any(Connection.class));
    }

    @Test
    @Description("Test attempting to pay a non-existent bill")
    public void testPayNonExistentBill() throws SQLException {
        when(billDAO.getBillsByIds(any(Connection.class), anyList())).thenReturn(Collections.emptyList());

        paymentService.payBills("1");

        verify(billDAO, never()).updateBillState(anyInt(), any(Connection.class));
        verify(billDAO, never()).updateCustomerBalance(anyDouble(), any(Connection.class));
        verify(billDAO, never()).addPayment(anyInt(), anyDouble(), any(Connection.class));
    }


    @Test
    @Description("Test paying bills with partial payment due to limited funds")
    public void testPayBillsWithPartialPayment() throws SQLException {
        List<Bill> bills = Arrays.asList(
                new Bill(1, "Utility", 100.0, "2024-07-21", "NOT_PAID", "Provider A"),
                new Bill(2, "Internet", 100.0, "2024-07-21", "NOT_PAID", "Provider B"),
                new Bill(3, "Water", 100.0, "2024-07-21", "NOT_PAID", "Provider C")
        );

        AtomicInteger callCount = new AtomicInteger(0);
        when(billDAO.getBillsByIds(any(Connection.class), anyList())).thenReturn(bills);
        when(billDAO.getBalance(any(Connection.class))).thenAnswer(invocation -> {
            int currentCount = callCount.incrementAndGet();
            double balance = 200.0;
            if (currentCount == 1) {
                return balance;
            } else if (currentCount == 2) {
                return balance - 100.0;
            } else {
                return balance - 200.0;
            }
        });

        when(billDAO.getBill(anyInt(), any(Connection.class))).thenAnswer(invocation -> {
            int id = invocation.getArgument(0);
            return bills.stream().filter(bill -> bill.getId() == id).findFirst().orElse(null);
        });

        doNothing().when(billDAO).updateBillState(anyInt(), any(Connection.class));
        doNothing().when(billDAO).updateCustomerBalance(anyDouble(), any(Connection.class));
        doNothing().when(billDAO).addPayment(anyInt(), anyDouble(), any(Connection.class));

        paymentService.payBills("1,2,3");

        verify(billDAO, times(2)).updateBillState(anyInt(), any(Connection.class));
        verify(billDAO, times(2)).updateCustomerBalance(anyDouble(), any(Connection.class));
        verify(billDAO, times(2)).addPayment(anyInt(), anyDouble(), any(Connection.class));
    }
}