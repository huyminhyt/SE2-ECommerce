package com.project.nike.controller;

import com.project.nike.dto.CartDto;
import com.project.nike.dto.OrderDto;
import com.project.nike.dto.OrderItemDto;
import com.project.nike.model.Cart;
import com.project.nike.model.Order;
import com.project.nike.model.OrderItem;
import com.project.nike.model.User;
import com.project.nike.repository.*;
import com.project.nike.response.ResponseHandler;
import com.project.nike.service.CartService;
import com.project.nike.service.VoucherService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/order/user")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CheckoutController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VoucherService voucherService;

    @GetMapping("/checkout")
    public ResponseEntity<Object> checkOutPage(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(username);
        Cart cart = cartRepository.findCartByUser(user.get());
        if(cart==null) {
            cart = new Cart();
            cart.setUser(userRepository.getUsersByEmail(username));
            cartRepository.save(cart);
        }
        CartDto cartDto = cartService.listCartItems(cart);
        if (cart.getVoucher() == null){
            return ResponseHandler.responseEntity("success", HttpStatus.ACCEPTED, cartDto);
        }
        cartDto.setVoucherId(cart.getVoucher().getId());
        cartDto.setTotalMoney(cartDto.getTotalMoney() - cartDto.getTotalMoney() * voucherService.getDiscountById(cart.getVoucher().getId()));
        return ResponseHandler.responseEntity("success", HttpStatus.ACCEPTED, cartDto);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Object> confirm(@RequestBody OrderDto orderDto){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()){
            return ResponseHandler.errorResponseEntity("bad", HttpStatus.BAD_REQUEST, "not found user!");
        }

        Order order = new Order();
        orderRepository.save(order);

        List<OrderItemDto> orderItemDto =  orderDto.getItemDtoList();
        List<OrderItem> orderItems = new ArrayList<>();
        double totalCost = 0;
        for (OrderItemDto each: orderItemDto) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(each.getId());
            orderItem.setProduct(productRepository.findById(each.getProductId()).orElseThrow(() -> new RuntimeException("Something error! ")));
            orderItem.setAmount(each.getAmount());
            orderItem.setTotalPaymentEachOrderItem(each.getTotalPaymentEachOrderItem());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
            totalCost += each.getTotalPaymentEachOrderItem();
            orderItemRepository.save(orderItem);
        }

        //order.setId(orderDto.getId());
        order.setOrderStatus("waiting for confirmation....");
        order.setUser(user.get());
        order.setAddress(orderDto.getAddress());
        order.setCreatedDate(new Date());
        order.setTotalPayment(totalCost - totalCost*(voucherService.getDiscountById(orderDto.getVoucherId())));
        orderRepository.save(order);

        //and remove everything from cart
        Cart cart = cartRepository.findCartByUser(user.get());
        cart.setVoucher(null);
        cartRepository.save(cart);

        //minus 1 out of quantity
        voucherService.decreaseQuantity(orderDto.getVoucherId());


        return ResponseHandler.responseEntity("oke", HttpStatus.OK, "order has been confirmed to process!");
    }
}
