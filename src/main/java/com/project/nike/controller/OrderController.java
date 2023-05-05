package com.project.nike.controller;

import com.project.nike.dto.ManagedOrderDto;
import com.project.nike.repository.UserRepository;
import com.project.nike.response.ResponseHandler;
import com.project.nike.service.OrderService;
import com.project.nike.model.User;
import jakarta.websocket.OnClose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/order/admin/unconfirmed")
    public ResponseEntity<Object> getUnconfirmedOrder(){

        List<ManagedOrderDto> list =orderService.getUnconfirmedOrder();

        return ResponseHandler.responseEntity("success", HttpStatus.OK, list);
    }

    @GetMapping("/api/order/admin/confirmed")
    public ResponseEntity<Object> getConfirmedOrder(){
        List<ManagedOrderDto> list =orderService.getConfirmedOrder();
        return ResponseHandler.responseEntity("success", HttpStatus.OK, list);
    }

    @GetMapping("/api/order/user/myorder")
    public ResponseEntity<Object> getMyOrder(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()){
            return ResponseHandler.errorResponseEntity("bad", HttpStatus.BAD_REQUEST, "not found user!");
        }
        List<ManagedOrderDto> list =orderService.getMyOrder(user.get());
        return ResponseHandler.responseEntity("success", HttpStatus.OK, list);
    }

    @PostMapping("/api/order/admin/accept/{id}")
    public ResponseEntity<Object> acceptOrder(@PathVariable long id){
        orderService.acceptOrder(id);
        return ResponseHandler.responseEntity("success", HttpStatus.OK, "order has been confirmed");
    }


    @PostMapping("/api/order/admin/cancel/{id}")
    public ResponseEntity<Object> adminCancelOrder(@PathVariable long id){
        orderService.cancelOrderById(id);
        return ResponseHandler.responseEntity("success", HttpStatus.OK, "order has been canceled");
    }

    @PostMapping("/api/order/user/cancel/{id}")
    public ResponseEntity<Object> userCancelOrder(@PathVariable long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()){
            return ResponseHandler.errorResponseEntity("bad", HttpStatus.BAD_REQUEST, "not found user!");
        }
        orderService.cancelOrderByUserAndId(user.get(), id);
        return ResponseHandler.responseEntity("success", HttpStatus.OK, "order has been canceled");
    }
}
