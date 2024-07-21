package org.example.commands;

import org.example.repositories.daos.BillDAO;
import org.example.models.Bill;
import org.example.utils.ConsoleUtils;

import java.util.List;

public class DueDateCmd implements Command<Void> {

    BillDAO billDAO;

    public DueDateCmd(BillDAO billDAO) {
        this.billDAO = billDAO;
    }

    @Override
    public Void execute(String... args) {
        List<Bill> bills = billDAO.getOverDueBills();
        ConsoleUtils.showBills(bills);
        return null;
    }
}
