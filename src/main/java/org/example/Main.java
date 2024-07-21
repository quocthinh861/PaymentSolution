package org.example;

import org.example.commands.Command;
import org.example.commands.ExitCmd;
import org.example.repositories.daos.CustomerDAO;
import org.example.factories.CommandFactory;
import org.example.factories.ConnectionFactory;
import org.example.models.Customer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void initializeDatabase(Connection conn) throws SQLException, IOException {
        Statement stmt = conn.createStatement();
        InputStream is = Main.class.getClassLoader().getResourceAsStream("database.sql");
        if (is != null) {
            String sql = new String(is.readAllBytes());
            stmt.execute(sql);
        } else {
            throw new RuntimeException("Failed to load database.sql");
        }
    }

    public static void main(String[] args) {
        System.out.println("\n\t****************************** Payment System ******************************\n");
        Scanner sc = new Scanner(System.in);
        CustomerDAO customerDAO = new CustomerDAO();

        try (Connection conn = ConnectionFactory.getConnection()) {
            System.out.println("Payment System connected to database.\n");
            initializeDatabase(conn);
            System.out.println("Populated database with mock data.\n");

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
                System.out.println("Enter 'exit' to exit the Payment System");
                Customer customer = customerDAO.getCustomerInfo();

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
