package com.project.nike.repository;

import com.project.nike.model.Cart;
import com.project.nike.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findCartByUser(User user);
    Cart getCartByUser(User user);
}
