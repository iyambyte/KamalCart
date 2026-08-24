package com.kamalbyte.KamalCart.seed;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.kamalbyte.KamalCart.entity.Order;
import com.kamalbyte.KamalCart.entity.OrderItem;
import com.kamalbyte.KamalCart.entity.Product;
import com.kamalbyte.KamalCart.entity.ProductReview;
import com.kamalbyte.KamalCart.repository.OrderRepository;
import com.kamalbyte.KamalCart.repository.ProductRepository;
import com.kamalbyte.KamalCart.repository.ProductReviewRepository;

@Component
@org.springframework.core.annotation.Order(2)
public class TestDataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final ProductReviewRepository productReviewRepository;
    private final OrderRepository orderRepository;

    public TestDataSeeder(ProductRepository productRepository,
            ProductReviewRepository productReviewRepository,
            OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.productReviewRepository = productReviewRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            System.out.println("No products found. Skipping related test data.");
            return;
        }

        seedReviews(products);
        seedOrders(products);
    }

    private void seedReviews(List<Product> products) {
        if (productReviewRepository.count() > 0) {
            System.out.println("Product reviews already exist. Skipping review seed.");
            return;
        }

        Product firstProduct = products.get(0);
        Product secondProduct = products.size() > 1 ? products.get(1) : firstProduct;

        ProductReview firstReview = new ProductReview(null, 5.0, "Excellent product for testing.");
        firstReview.setProduct(firstProduct);
        ProductReview secondReview = new ProductReview(null, 4.0, "Good quality and fast delivery.");
        secondReview.setProduct(secondProduct);

        productReviewRepository.saveAll(List.of(firstReview, secondReview));
        System.out.println("Seeded test product reviews.");
    }

    private void seedOrders(List<Product> products) {
        if (orderRepository.count() > 0) {
            System.out.println("Orders already exist. Skipping order seed.");
            return;
        }

        Product firstProduct = products.get(0);
        Product secondProduct = products.size() > 1 ? products.get(1) : firstProduct;

        Order order = new Order();
        order.setReferenceId("TEST-ORDER-001");
        order.setStatus("PLACED");
        order.setTotalItemsAmount(firstProduct.getPrice() + secondProduct.getPrice());
        order.setTaxAmount(50.0);
        order.setTotalAmount(order.getTotalItemsAmount() + order.getTaxAmount());

        OrderItem firstItem = new OrderItem(firstProduct.getName(), 1, null,
                firstProduct.getPrice(), firstProduct);
        firstItem.setOrder(order);
        OrderItem secondItem = new OrderItem(secondProduct.getName(), 1, null,
                secondProduct.getPrice(), secondProduct);
        secondItem.setOrder(order);
        order.setOrderItems(List.of(firstItem, secondItem));

        orderRepository.save(order);
        System.out.println("Seeded test order and order items.");
    }
}
