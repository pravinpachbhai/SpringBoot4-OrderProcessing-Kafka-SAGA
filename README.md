# SpringBoot4-OrderProcessing-Kafka-SAGA

# Because of the limited resources, I am creating all the services in one project
# Please create different microservices for each service below and the related database.
# Clear responsibilities, APIs, and database design per service.

# Download the kafka -  kafka_2.13-4.2.0
# Step 1: Generate Cluster ID -   bin/kafka-storage.sh random-uuid
# Step 2: Format Storage - bin/kafka-storage.sh format --standalone -t ADD_YOUR_CLUSTER_ID -c config/server.properties
# Step 3: Start Kafka - bin/kafka-server-start.sh config/server.properties

# OR

# Start with Daemon Mode
# bin/kafka-server-start.sh -daemon config/server.properties

# Stop Kafka with Ctrl + C
# TO CHECK ALL TOPIC - bin/kafka-topics.sh --bootstrap-server localhost:9092 --list

# bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic order.created
# bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic inventory.reserved
# bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic payment.completed
# bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic shipment.created


<br/>

1 - UserService<br/>
2 - OrderService<br/>
3 - ProductService<br/>
4 - InventoryService<br/>
5 - PaymentService<br/>
6 - ShippingService<br/>
7 - EmailNotificationService<br/>
<br/><br/>

| Service                  | DB | Responsibility |
|--------------------------| -------- | -------- |
| UserService              |	user_db	| User management
| OrderService             |	order_db	| Orders
| ProductService           |	product_db	| Products
| InventoryService	        | inventory_db	| Stock
| PaymentService	          | payment_db	| Payments
| ShippingService	         | shipping_db	| Delivery
| EmailNotificationService |	notification_db	| Notifications

<br/><br/>
<img src="src/main/resources/static/Diagram.jpg" alt="Description" width="800">

<br/><br/>
# SpringBoot4-Dashboard-Inventory-Management-API
https://github.com/pravinpachbhai/SpringBoot4-Dashboard-Inventory-Management-API



