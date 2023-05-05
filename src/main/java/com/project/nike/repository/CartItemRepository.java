package com.project.nike.repository;

import com.project.nike.model.Cart;
import com.project.nike.model.CartItem;
import com.project.nike.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByProductAndCart(Product product, Cart cart);
    List<CartItem> findByCart(Cart cart);

}
