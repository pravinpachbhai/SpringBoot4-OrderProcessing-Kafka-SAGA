package com.pravin.kafka.component;

import com.pravin.kafka.dto.AddressRequest;
import com.pravin.kafka.dto.AddressResponse;
import com.pravin.kafka.dto.InventoryResponse;
import com.pravin.kafka.dto.NotificationRequest;
import com.pravin.kafka.dto.NotificationResponse;
import com.pravin.kafka.dto.OrderItemRequest;
import com.pravin.kafka.dto.OrderItemResponse;
import com.pravin.kafka.dto.OrderRequest;
import com.pravin.kafka.dto.OrderResponse;
import com.pravin.kafka.dto.PaymentRequest;
import com.pravin.kafka.dto.PaymentResponse;
import com.pravin.kafka.dto.ProductResponse;
import com.pravin.kafka.dto.ShipmentRequest;
import com.pravin.kafka.dto.ShipmentResponse;
import com.pravin.kafka.dto.UserRequest;
import com.pravin.kafka.dto.UserResponse;
import com.pravin.kafka.entity.Address;
import com.pravin.kafka.entity.Inventory;
import com.pravin.kafka.entity.Notification;
import com.pravin.kafka.entity.Order;
import com.pravin.kafka.entity.OrderItem;
import com.pravin.kafka.entity.OrderStatus;
import com.pravin.kafka.entity.Payment;
import com.pravin.kafka.entity.PaymentStatus;
import com.pravin.kafka.entity.Product;
import com.pravin.kafka.entity.Shipment;
import com.pravin.kafka.entity.ShipmentStatus;
import com.pravin.kafka.entity.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-01T14:37:23-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
)
@Component
public class DataMapperImpl implements DataMapper {

    @Override
    public User toEntity(UserRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setName( request.name() );
        user.setEmail( request.email() );
        user.setAddresses( addressRequestListToAddressList( request.addresses() ) );

        return user;
    }

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        List<AddressResponse> addresses = null;
        Long id = null;
        String name = null;
        String email = null;
        LocalDateTime createdAt = null;

        addresses = addressListToAddressResponseList( user.getAddresses() );
        id = user.getId();
        name = user.getName();
        email = user.getEmail();
        createdAt = user.getCreatedAt();

        UserResponse userResponse = new UserResponse( id, name, email, createdAt, addresses );

        return userResponse;
    }

    @Override
    public Address toEntity(AddressRequest request) {
        if ( request == null ) {
            return null;
        }

        Address address = new Address();

        address.setAddressLine( request.addressLine() );
        address.setCity( request.city() );
        address.setState( request.state() );
        address.setZip( request.zip() );

        return address;
    }

    @Override
    public AddressResponse toResponse(Address address) {
        if ( address == null ) {
            return null;
        }

        Long id = null;
        String addressLine = null;
        String city = null;
        String state = null;
        String zip = null;

        id = address.getId();
        addressLine = address.getAddressLine();
        city = address.getCity();
        state = address.getState();
        zip = address.getZip();

        AddressResponse addressResponse = new AddressResponse( id, addressLine, city, state, zip );

        return addressResponse;
    }

    @Override
    public Order toEntity(OrderRequest request) {
        if ( request == null ) {
            return null;
        }

        Order order = new Order();

        order.setUserId( request.userId() );
        order.setItems( orderItemRequestListToOrderItemList( request.items() ) );

        order.setStatus( OrderStatus.CREATED );

        return order;
    }

    @Override
    public OrderResponse toResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        Long id = null;
        Long userId = null;
        OrderStatus status = null;
        BigDecimal totalAmount = null;
        LocalDateTime createdAt = null;
        List<OrderItemResponse> items = null;

        id = order.getId();
        userId = order.getUserId();
        status = order.getStatus();
        totalAmount = order.getTotalAmount();
        createdAt = order.getCreatedAt();
        items = orderItemListToOrderItemResponseList( order.getItems() );

        OrderResponse orderResponse = new OrderResponse( id, userId, status, totalAmount, createdAt, items );

        return orderResponse;
    }

    @Override
    public OrderItem toEntity(OrderItemRequest request) {
        if ( request == null ) {
            return null;
        }

        OrderItem orderItem = new OrderItem();

        orderItem.setProductId( request.productId() );
        orderItem.setQuantity( request.quantity() );
        orderItem.setPrice( request.price() );

        return orderItem;
    }

    @Override
    public OrderItemResponse toResponse(OrderItem item) {
        if ( item == null ) {
            return null;
        }

        Long id = null;
        Long productId = null;
        Integer quantity = null;
        BigDecimal price = null;

        id = item.getId();
        productId = item.getProductId();
        quantity = item.getQuantity();
        price = item.getPrice();

        OrderItemResponse orderItemResponse = new OrderItemResponse( id, productId, quantity, price );

        return orderItemResponse;
    }

    @Override
    public InventoryResponse toResponse(Inventory inventory) {
        if ( inventory == null ) {
            return null;
        }

        Long productId = null;
        Integer availableQuantity = null;
        Integer reservedQuantity = null;

        productId = inventory.getProductId();
        availableQuantity = inventory.getAvailableQuantity();
        reservedQuantity = inventory.getReservedQuantity();

        InventoryResponse inventoryResponse = new InventoryResponse( productId, availableQuantity, reservedQuantity );

        return inventoryResponse;
    }

    @Override
    public Payment toEntity(PaymentRequest request) {
        if ( request == null ) {
            return null;
        }

        Payment payment = new Payment();

        payment.setOrderId( request.orderId() );
        payment.setAmount( request.amount() );

        return payment;
    }

    @Override
    public PaymentResponse toResponse(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        Long id = null;
        Long orderId = null;
        PaymentStatus status = null;
        BigDecimal amount = null;
        LocalDateTime createdAt = null;

        id = payment.getId();
        orderId = payment.getOrderId();
        status = payment.getStatus();
        amount = payment.getAmount();
        createdAt = payment.getCreatedAt();

        PaymentResponse paymentResponse = new PaymentResponse( id, orderId, status, amount, createdAt );

        return paymentResponse;
    }

    @Override
    public Shipment toEntity(ShipmentRequest request) {
        if ( request == null ) {
            return null;
        }

        Shipment shipment = new Shipment();

        shipment.setOrderId( request.orderId() );

        shipment.setStatus( ShipmentStatus.CREATED );

        return shipment;
    }

    @Override
    public ShipmentResponse toResponse(Shipment shipment) {
        if ( shipment == null ) {
            return null;
        }

        Long id = null;
        Long orderId = null;
        ShipmentStatus status = null;
        String trackingNumber = null;
        LocalDateTime createdAt = null;

        id = shipment.getId();
        orderId = shipment.getOrderId();
        status = shipment.getStatus();
        trackingNumber = shipment.getTrackingNumber();
        createdAt = shipment.getCreatedAt();

        ShipmentResponse shipmentResponse = new ShipmentResponse( id, orderId, status, trackingNumber, createdAt );

        return shipmentResponse;
    }

    @Override
    public Notification toEntity(NotificationRequest request) {
        if ( request == null ) {
            return null;
        }

        Notification notification = new Notification();

        notification.setType( request.type() );
        notification.setRecipient( request.recipient() );
        notification.setMessage( request.message() );

        notification.setStatus( "SENT" );

        return notification;
    }

    @Override
    public NotificationResponse toResponse(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        Long id = null;
        String type = null;
        String recipient = null;
        String status = null;
        String message = null;
        LocalDateTime sentAt = null;

        id = notification.getId();
        type = notification.getType();
        recipient = notification.getRecipient();
        status = notification.getStatus();
        message = notification.getMessage();
        sentAt = notification.getSentAt();

        NotificationResponse notificationResponse = new NotificationResponse( id, type, recipient, status, message, sentAt );

        return notificationResponse;
    }

    @Override
    public ProductResponse toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        Long id = null;
        String code = null;
        String name = null;
        BigDecimal price = null;
        LocalDateTime created = null;
        LocalDateTime updated = null;

        id = product.getId();
        code = product.getCode();
        name = product.getName();
        price = product.getPrice();
        created = product.getCreated();
        updated = product.getUpdated();

        ProductResponse productResponse = new ProductResponse( id, code, name, price, created, updated );

        return productResponse;
    }

    protected List<Address> addressRequestListToAddressList(List<AddressRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<Address> list1 = new ArrayList<Address>( list.size() );
        for ( AddressRequest addressRequest : list ) {
            list1.add( toEntity( addressRequest ) );
        }

        return list1;
    }

    protected List<AddressResponse> addressListToAddressResponseList(List<Address> list) {
        if ( list == null ) {
            return null;
        }

        List<AddressResponse> list1 = new ArrayList<AddressResponse>( list.size() );
        for ( Address address : list ) {
            list1.add( toResponse( address ) );
        }

        return list1;
    }

    protected List<OrderItem> orderItemRequestListToOrderItemList(List<OrderItemRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItem> list1 = new ArrayList<OrderItem>( list.size() );
        for ( OrderItemRequest orderItemRequest : list ) {
            list1.add( toEntity( orderItemRequest ) );
        }

        return list1;
    }

    protected List<OrderItemResponse> orderItemListToOrderItemResponseList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemResponse> list1 = new ArrayList<OrderItemResponse>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( toResponse( orderItem ) );
        }

        return list1;
    }
}
