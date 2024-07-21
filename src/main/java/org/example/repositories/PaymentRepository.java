package org.example.repositories;

import org.example.models.Payment;

import java.sql.SQLException;
import java.util.List;

public interface PaymentRepository {
    List<Payment> listPayments() throws SQLException;
}
