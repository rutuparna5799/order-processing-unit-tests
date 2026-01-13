package com.company.order_processing_engine.service;

import com.company.order_processing_engine.exception.*;
import com.company.order_processing_engine.model.Order;
import com.company.order_processing_engine.model.OrderItem;
import com.company.order_processing_engine.model.OrderStatus;
import com.company.order_processing_engine.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

public class OrderServiceImpl {

    private final PricingService pricingService;
    private final ComplianceService complianceService;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(PricingService pricingService,
                            ComplianceService complianceService,
                            OrderRepository orderRepository) {
        this.pricingService = pricingService;
        this.complianceService = complianceService;
        this.orderRepository = orderRepository;
    }



    public Order createOrder(Order order) {
        if (!order.getCustomer().isActive()) {
            throw new CustomerInactiveException();
        }

        if(order.getItems() == null || order.getItems().isEmpty())
        {
            throw new EmptyOrderItemsException();
        }
        double total=0;

       for (OrderItem item : order.getItems()){
           if(item.getPrice()==0){
               throw new InvalidItemPriceException();
           }
           if(item.getQuantity()==0){
               throw new InvalidQuantityException();
           }
           total = total+ item.getPrice()* item.getQuantity();


       }

        order.setTotalAmount(total);
        complianceService.validate(order);
        order.setStatus(OrderStatus.CREATED);

        // 5️⃣ Save and return
        return orderRepository.save(order);


    }

    public Order cancelOrder(String orderId, String reason) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(orderId==null || optionalOrder.isEmpty()){
            throw new OrderNotFoundException();
        }

        Order order = optionalOrder.get();

//        //check if order is cancelled already
//        if (order.getStatus() == OrderStatus.CANCELLED) {
//            throw new RuntimeException("Order already cancelled");
//        }

        // Check if order is in CREATED state
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new InvalidOrderStateException();
        }

        // Reason must be provided
        if (reason == null || reason.isBlank()) {
            throw new MissingCancellationReasonException();
        }

        order.setStatus(OrderStatus.CANCELLED);

        return orderRepository.save(order);

    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException());
    }

    public java.util.List<Order> getOrdersByCustomer(String customerId) {
        if(customerId == null || customerId.isBlank())
        {
            throw  new CustomerIdRequiredException();
        }

        List<Order> orders= orderRepository.findByCustomerId(customerId);
      return orders == null ? List.of() : orders;
    }
}
