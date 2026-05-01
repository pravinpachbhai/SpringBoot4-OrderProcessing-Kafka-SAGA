INSERT INTO users (id, name, email, password_hash, created_at)
VALUES (1, 'Pravin Pachbhai', 'pravin_pachbhai@yahoo.com', '123456', CURRENT_TIMESTAMP),
       (2, 'Raahi Pachbhai', 'raahi_pachbhai@yahoo.com', '12345', CURRENT_TIMESTAMP);

INSERT INTO addresses (id, user_id, address_line, city, state, zip)
VALUES (1, 1, '10475 Garibaldi Pl', 'St Louis', 'MO', '63131'),
       (2, 2, '10475 Garibaldi Pl', 'St Louis', 'MO', '63131');



INSERT INTO orders (id, user_id, status, total_amount, created_at)
VALUES (1, 1, 'CREATED', 150.00, CURRENT_TIMESTAMP),
       (2, 2, 'CONFIRMED', 200.00, CURRENT_TIMESTAMP);

INSERT INTO order_items (id, order_id, product_id, quantity, price)
VALUES (1, 1, 101, 2, 50.00),
       (2, 1, 102, 1, 50.00),
       (3, 2, 103, 4, 50.00);


INSERT INTO products (id, code, name, price)
VALUES (1, 'P001', 'Laptop', 799.99);
INSERT INTO products (id, code, name, price)
VALUES (2, 'P002', 'Smartphone', 599.49);
INSERT INTO products (id, code, name, price)
VALUES (3, 'P003', 'Tablet', 399.00);
INSERT INTO products (id, code, name, price)
VALUES (4, 'P004', 'Monitor', 199.99);
INSERT INTO products (id, code, name, price)
VALUES (5, 'P005', 'Keyboard', 49.99);

INSERT INTO inventory (product_id, available_quantity, reserved_quantity)
VALUES (1, 10, 2),
       (2, 50, 5),
       (3, 30, 10),
       (4, 100, 30),
       (5, 500, 50);

INSERT INTO payments (id, order_id, status, amount, created_at)
VALUES (1, 1, 'PENDING', 150.00, CURRENT_TIMESTAMP),
       (2, 2, 'SUCCESS', 200.00, CURRENT_TIMESTAMP);

INSERT INTO shipments (id, order_id, status, tracking_number, created_at)
VALUES (1, 2, 'SHIPPED', 'TRACK123456', CURRENT_TIMESTAMP),
       (2, 2, 'DELIVERED', 'TRACK654321', CURRENT_TIMESTAMP);

INSERT INTO notifications (id, type, recipient, status, message, sent_at)
VALUES (1, 'ORDER_CONFIRMATION', 'pravin_pachbhai@yahoo.com', 'SENT', 'Your order has been created.', CURRENT_TIMESTAMP),
       (2, 'PAYMENT_SUCCESS', 'pravin_pachbhai@yahoo.com', 'SENT', 'Your payment was successful.', CURRENT_TIMESTAMP),
       (3, 'SHIPPING_UPDATE', 'pravin_pachbhai@yahoo.com', 'SENT', 'Your order has been shipped.', CURRENT_TIMESTAMP);