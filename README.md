# Payment System

## Overview

The Payment System is a simple Java-based application designed to manage and automate bill payments for customers. It includes features for scheduling payments, managing customer data, and handling bill payments.

## Features

- **Customer Management**: View and manage customer details and balances.
- **Bill Management**: Create and manage bills associated with customers.
- **Payment Management**: Track and process payments for bills.
- **Scheduled Payments**: Automate bill payments based on a schedule.

## System Components

### Database Schema

- **customers**: Table to store customer information.
- **bills**: Table to store bill information with a foreign key to `customers`.
- **payments**: Table to store payment transactions with foreign keys to `customers` and `bills`.
- **scheduled_payments**: Table to store scheduled payments with foreign keys to `customers` and `bills`.

### Java Application

- **Main Class**: Entry point for the application.
- **Command Classes**: Implement different commands such as `PayCmd`, `ListBillCmd`, `AddFundCmd`, `ExitCmd`.
- **DAO Classes**: Data Access Object classes to handle database operations for `Customer`, `Bill`, `Payment`, etc.
- **Factories**: Factory classes for creating command instances and managing database connections.
- **UserContext**: Manages the current user context (e.g., user ID).

## Database Setup

### SQL Schema

```sql
-- Create customers table
CREATE TABLE IF NOT EXISTS customers (
    id INT AUTO_INCREMENT,
    balance DECIMAL(10, 2),
    PRIMARY KEY (id)
);

-- Create bills table with a foreign key referencing customers
CREATE TABLE IF NOT EXISTS bills (
    id INT AUTO_INCREMENT,
    type VARCHAR(255),
    amount DECIMAL(10, 2),
    due_date DATE,
    state VARCHAR(255),
    provider VARCHAR(255),
    customer_id INT,
    PRIMARY KEY (id),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Create payments table with foreign keys referencing customers and bills
CREATE TABLE IF NOT EXISTS payments (
    id INT AUTO_INCREMENT,
    payment_date DATE,
    customer_id INT,
    bill_id INT,
    PRIMARY KEY (id),
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (bill_id) REFERENCES bills(id)
);

-- Create scheduled_payments table
CREATE TABLE IF NOT EXISTS scheduled_payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    bill_id INT,
    schedule_date DATETIME,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (bill_id) REFERENCES bills(id)
);

-- Insert initial data
INSERT INTO customers (balance) VALUES (1000.00);

INSERT INTO bills (type, amount, due_date, state, provider, customer_id) VALUES 
    ('ELECTRIC', 100.00, '2020-10-25', 'NOT_PAID', 'EVN HCMC', 1),
    ('WATER', 200.00, '2020-10-30', 'NOT_PAID', 'SAVACO HCMC', 1),
    ('INTERNET', 300.00, '2020-11-30', 'NOT_PAID', 'VNPT', 1),
    ('GAME', 1500.00, '2024-09-21', 'NOT_PAID', 'PROVIDER_B', 1),
    ('MEAL', 400.00, '2025-11-30', 'NOT_PAID', 'PROVIDER_A', 1);

-- Insert scheduled payments
INSERT INTO scheduled_payments (customer_id, bill_id, schedule_date) VALUES 
    (1, 1, '2024-07-30 10:00:00'),
    (1, 2, '2024-08-01 11:00:00'),
    (1, 3, '2024-08-05 12:00:00'),
    (1, 4, '2024-09-20 09:00:00'),
    (1, 5, '2025-11-29 08:00:00');

```

## Java Application Setup

### Prerequisites

Before running the application, ensure that you have the following:

- **Java Development Kit (JDK)**: Ensure you have JDK 8 or higher installed. You can download it from [Oracle's website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or use an open-source distribution like [OpenJDK](https://openjdk.java.net/).
- **Maven**: Apache Maven is used for managing project dependencies and building the application. Install Maven from [the official site](https://maven.apache.org/install.html).

### Project Structure

The Java application is structured as follows:

- **`src/main/java`**: Contains the main application source code, including classes for commands, data access objects (DAOs), and utility classes.
- **`src/main/resources`**: Contains configuration files and SQL scripts.
- **`pom.xml`**: Maven build file that specifies project dependencies and plugins.

### Setup and Configuration

1. **Clone the Repository**

   Clone the project repository to your local machine using Git:

   ```bash
   git clone <repository-url>
   cd <repository-directory>
    ```
   
2. **Build the Application**

   Use Maven to build the application:

   ```bash
   mvn clean install
   ```
   
3. **Run the Application**

   Run the application using the following command:

   ```bash
   java -jar target/PaymentSolution-1.0-SNAPSHOT.jar
   ```
   
4. **Interact with the Application**

   Once the application is running, you can interact with it using the command-line interface. Use the available commands to manage customers, bills, and payments.

   ```bash
   help # Display available commands
   ```

   ```bash
   add 500 # Add funds to the current user's balance
   ```

   ```bash
   pay 1 2 3 # Pay bills with IDs 1, 2, and 3
   ```

   ```bash
   list_bill # List all bills for the current user
   ```

   ```bash
   schedule 1 22/09/2024 # Schedule a payment for bill ID 1 on 22/09/2024
   ```

   ```bash
    exit # Exit the application
   ```

## Troubleshooting

- **Database Connection Issues**: This is embedded database, so no need to worry about connection credentials.
- **Runtime Exceptions**: Review the console output for any exceptions or errors and address them accordingly.


