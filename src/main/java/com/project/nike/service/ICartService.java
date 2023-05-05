package com.project.nike.service;

import com.project.nike.dto.CartDto;
import com.project.nike.model.Cart;

public interface ICartService {
    public CartDto listCartItems(Cart cart);
}



