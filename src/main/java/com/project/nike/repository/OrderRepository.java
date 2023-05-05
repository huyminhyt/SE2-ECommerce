package com.project.nike.repository;

import com.project.nike.model.Order;
import com.project.nike.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    public List<Order> findByOrderStatus(String status);
    public List<Order> findByUser(User user);
    public Order findOrderByUserAndId(User user, long id);
    public int countByOrderStatus(String status);
}
