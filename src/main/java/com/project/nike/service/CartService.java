package com.project.nike.service;


import com.project.nike.dto.CartDto;
import com.project.nike.dto.CartItemDto;
import com.project.nike.model.Cart;
import com.project.nike.model.CartItem;
import com.project.nike.repository.CartItemRepository;
import com.project.nike.repository.CartRepository;
import com.project.nike.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService implements ICartService{
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Override
    public CartDto listCartItems(Cart cart) {
        List<CartItem> cartItemList = cartItemRepository.findByCart(cart);
        CartDto cartDto = new CartDto();
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        double totalCost = 0;
        for (CartItem ci: cartItemList) {
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setId(ci.getId());
            cartItemDto.setProductId(ci.getProduct().getId());
            cartItemDto.setUnitPrice(productRepository.findById(ci.getProduct().getId()).get().getProductPrice());
            cartItemDto.setQuantity(ci.getQuantity());
            cartItemDto.setTotalPaymentEachCartItem(ci.getQuantity()*ci.getProduct().getProductPrice());
            cartItemDtos.add(cartItemDto);
            totalCost += ci.getQuantity()*ci.getProduct().getProductPrice();
        }
        cartDto.setCartItemDtos(cartItemDtos);
        cartDto.setTotalMoney(totalCost);
        //maybe discard
        //cartDto.setVoucherId(cart.getVoucher().getId());
        return cartDto;
    }
}



