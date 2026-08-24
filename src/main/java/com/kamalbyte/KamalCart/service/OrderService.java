package com.kamalbyte.KamalCart.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamalbyte.KamalCart.entity.Order;
import com.kamalbyte.KamalCart.entity.OrderItem;
import com.kamalbyte.KamalCart.entity.Product;
import com.kamalbyte.KamalCart.repository.OrderRepository;
import com.kamalbyte.KamalCart.repository.ProductRepository;
import com.kamalbyte.KamalCart.dto.CreateOrderRequest;
import com.kamalbyte.KamalCart.dto.OrderCreated;
import com.kamalbyte.KamalCart.dto.OrderItemDto;

@Service
public class OrderService {
    @Autowired
    private ProductRepository prodRepo;

    @Autowired
    private OrderRepository orderRepo;

    public OrderCreated createOrder(CreateOrderRequest orderRequest) {
        Order order = new Order();
        order.setStatus("PENDING");
        double totalItemsAmount = 0;

        for (OrderItemDto item : orderRequest.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setName(item.getName());
            orderItem.setPrice(item.getPrice());
            orderItem.setImage(item.getImage());
            orderItem.setQuantity(item.getQuantity());

            Product product = prodRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            orderItem.setProduct(product);
            totalItemsAmount += item.getPrice() * item.getQuantity();

            order.getOrderItems().add(orderItem);

        }

        order.setTotalItemsAmount(totalItemsAmount);
        double totalAmount = 0;
        double taxAmount = 10;
        totalAmount = totalItemsAmount + taxAmount;
        order.setTotalAmount(totalAmount);
        order.setTaxAmount(taxAmount);
        String refId = UUID.randomUUID().toString();
        order.setReferenceId(refId);
        orderRepo.save(order);
        return new OrderCreated(refId);

    }

    public Order getOrder(String referenceId) {
        return orderRepo.findByReferenceId(referenceId)
                .orElseThrow(() -> new RuntimeException("No order found with Ref Id"));
    }
}
