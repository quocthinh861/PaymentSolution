package org.example;

import org.example.commands.Command;
import org.example.commands.ExitCmd;
import org.example.repositories.daos.BillDAO;
import org.example.repositories.daos.CustomerDAO;
import org.example.factories.CommandFactory;
import org.example.factories.ConnectionFactory;
import org.example.models.Customer;
import org.example.repositories.daos.PaymentDAO;
import org.example.services.PaymentProcessorTask;
import org.example.services.PaymentService;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;

public class Main {

    public static void loadScheduledPayments(Connection conn) {
        PaymentService paymentService = new PaymentService(
                new BillDAO(),
                new PaymentDAO()
        );
        String sql = "SELECT * FROM scheduled_payments WHERE schedule_date > CURRENT_DATE";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int billId = rs.getInt("bill_id");
                Date paymentDate = rs.getDate("schedule_date"); // Use Timestamp for DATETIME columns

                Timer timer = new Timer();
                timer.schedule(new PaymentProcessorTask(paymentService, billId), paymentDate);
            }
            System.out.println("Scheduled payments loaded successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void initializeDatabase(Connection conn) throws SQLException, IOException {
        Statement stmt = conn.createStatement();
        InputStream is = Main.class.getClassLoader().getResourceAsStream("database.sql");
        if (is != null) {
            String sql = new String(is.readAllBytes());
            stmt.execute(sql);
            System.out.println("Database initialized successfully.");
        } else {
            throw new RuntimeException("Failed to load database.sql");
        }
    }

    public static void main(String[] args) {
        System.out.println("\n\t****************************** Payment System ******************************\n");
        Scanner sc = new Scanner(System.in);
        CustomerDAO customerDAO = new CustomerDAO();

        try (Connection conn = ConnectionFactory.getConnection()) {
            initializeDatabase(conn);
            loadScheduledPayments(conn);

            while (true) {
                try {
                    System.out.println("Choose a user id (Default: 1)");
                    List<Customer> customers = customerDAO.getCustomers();
                    for (Customer customer : customers) {
                        System.out.println("User ID: " + customer.getId() + " | Balance: " + customer.getBalance());
                    }

                    System.out.print("> ");
                    String userIdInput = sc.nextLine();
                    int userId = userIdInput.isEmpty() ? 1 : Integer.parseInt(userIdInput);
                    UserContext.getInstance().setUserId(userId);

                    Customer customer = customerDAO.getCustomerInfo();
                    if (customer == null) {
                        throw new RuntimeException("Invalid user id");
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid user id. Please try again.\n");
                    sc.nextLine();
                }
            }

            while (true) {
                Customer customer = customerDAO.getCustomerInfo();
                System.out.println("Enter 'help' to see available commands\n");
                System.out.println("Your current balance is: " + customer.getBalance() + "\n");
                System.out.print("> ");

                String input = sc.nextLine();
                input = input.trim().toLowerCase();
                String[] commandInputs = input.split("\\s+");
                String command = commandInputs[0];
                String[] arguments = Arrays.copyOfRange(commandInputs, 1, commandInputs.length);

                Command cmd = CommandFactory.createCommand(command);
                try {
                    if (cmd != null) {
                        if (cmd instanceof ExitCmd) {
                            boolean shouldExit = (Boolean) cmd.execute(commandInputs);
                            if (shouldExit) {
                                System.out.println("Goodbye!");
                                break;
                            }
                        } else {
                            cmd.execute(arguments);
                        }
                    } else {
                        System.out.println("Invalid command. Please try again.\n");
                    }
                } catch (Exception e) {
                    System.out.println("An error occurred. Please try again.\n");
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Database connection could not be established.\n");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR: Failed to load database.sql\n");
            e.printStackTrace();
        }
    }
}
