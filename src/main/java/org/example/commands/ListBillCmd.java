package org.example.commands;

import org.example.models.Bill;
import org.example.repositories.daos.BillDAO;
import org.example.utils.ConsoleUtils;

import java.util.List;

public class ListBillCmd implements Command<Void> {

    private BillDAO billDAO;

    public ListBillCmd(BillDAO billDAO) {
        this.billDAO = billDAO;
    }

    @Override
    public Void execute(String... args) {
        List<Bill> bills = billDAO.getAllBills();
        ConsoleUtils.showBills(bills);
        return null;
    }
}
