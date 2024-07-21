package org.example.factories;

import org.example.commands.*;
import org.example.repositories.daos.BillDAO;
import org.example.repositories.daos.CustomerDAO;
import org.example.repositories.daos.PaymentDAO;
import org.example.enums.CommandEnum;
import org.example.services.PaymentService;

public class CommandFactory {

    private static final CustomerDAO customerDAO = new CustomerDAO();
    private static final BillDAO billDAO = new BillDAO();
    private static final PaymentDAO paymentDAO = new PaymentDAO();
    private static final PaymentService paymentService = new PaymentService(billDAO, paymentDAO);

    public static Command createCommand(CommandEnum commandEnum) {

        switch (commandEnum) {
            case PAY:
                return new PayCmd(paymentService);
            case LIST_BILL:
                return new ListBillCmd(billDAO);
            case ADD_FUND:
                return new AddFundCmd(customerDAO);
            case DUE_DATE:
                return new DueDateCmd(billDAO);
                case LIST_PAYMENT:
                return new ListPaymentCmd(paymentService);
            case EXIT:
                return new ExitCmd();
            default:

        }
        return null;
    }

    public static Command createCommand(String command) {
        CommandEnum commandEnum = CommandEnum.fromString(command);
        if (commandEnum == null) {
            return null;
        }
        return createCommand(CommandEnum.fromString(command));
    }
}
