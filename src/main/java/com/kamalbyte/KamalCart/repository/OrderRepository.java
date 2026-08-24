package com.kamalbyte.KamalCart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kamalbyte.KamalCart.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByReferenceId(String referenceId);
}
