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

CREATE TABLE IF NOT EXISTS scheduled_payments (
                                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                                  customer_id INT,
                                                  bill_id INT,
    schedule_date DATETIME,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (bill_id) REFERENCES bills(id)
    );

-- Insert a customer with an initial balance
INSERT INTO customers (balance) VALUES (1000.00);

-- Insert bills associated with the customer (customer_id = 1)
INSERT INTO bills (type, amount, due_date, state, provider, customer_id) VALUES ('ELECTRIC', 100.00, '2020-10-25', 'NOT_PAID', 'EVN HCMC', 1);
INSERT INTO bills (type, amount, due_date, state, provider, customer_id) VALUES ('WATER', 200.00, '2020-10-30', 'NOT_PAID', 'SAVACO HCMC', 1);
INSERT INTO bills (type, amount, due_date, state, provider, customer_id) VALUES ('INTERNET', 300.00, '2020-11-30', 'NOT_PAID', 'VNPT', 1);
INSERT INTO bills (type, amount, due_date, state, provider, customer_id) VALUES ('GAME', 1500.00, '2024-09-21', 'NOT_PAID', 'PROVIDER_B', 1);
INSERT INTO bills (type, amount, due_date, state, provider, customer_id) VALUES ('MEAL', 400.00, '2025-11-30', 'NOT_PAID', 'PROVIDER_A', 1);


INSERT INTO scheduled_payments (customer_id, bill_id, schedule_date) VALUES (1, 1, '2026-07-30');
INSERT INTO scheduled_payments (customer_id, bill_id, schedule_date) VALUES (1, 2, '2026-08-01');
INSERT INTO scheduled_payments (customer_id, bill_id, schedule_date) VALUES (1, 3, '2026-08-05');
INSERT INTO scheduled_payments (customer_id, bill_id, schedule_date) VALUES (1, 4, '2026-09-20');
INSERT INTO scheduled_payments (customer_id, bill_id, schedule_date) VALUES (1, 5, '2026-11-29');