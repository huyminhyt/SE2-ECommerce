package com.project.nike.service;


import com.project.nike.dto.ManagedOrderDto;
import com.project.nike.dto.OrderItemDto;
import com.project.nike.model.Order;
import com.project.nike.model.OrderItem;
import com.project.nike.model.User;
import com.project.nike.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;







@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<ManagedOrderDto> getMyOrder(User user) {
        List<Order> orders = orderRepository.findByUser(user);
        return filter(orders);
    }

    public List<ManagedOrderDto> getUnconfirmedOrder() {
        List<Order> orders = orderRepository.findByOrderStatus("waiting for confirmation....");
        return filter(orders);
    }

    public List<ManagedOrderDto> getConfirmedOrder() {
        List<Order> orders = orderRepository.findByOrderStatus("confirmed");
        return filter(orders);
    }

    public void acceptOrder(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            //handle exception
        }
        Order findedOrder = order.get();
        if (order.get().getOrderStatus().equals("waiting for confirmation....")) {

            //handle exception
        }
        findedOrder.setOrderStatus("confirmed");
        orderRepository.save(findedOrder);
    }

    public void cancelOrderById(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            //handle exception
        }
        Order findedOrder = order.get();
        if (order.get().getOrderStatus().equals("waiting for confirmation....")) {
            //handle exception
        }
        findedOrder.setOrderStatus("canceled");
        orderRepository.save(findedOrder);
    }

    public void cancelOrderByUserAndId(User user, long id) {
        Order order = orderRepository.findOrderByUserAndId(user, id);
        if (order == null) {
            //handle exception
        }
        if (order.getOrderStatus().equals("waiting for confirmation....")) {
            //handle exception
        }
        order.setOrderStatus("canceled");
        orderRepository.save(order);
    }

    private List<ManagedOrderDto> filter(List<Order> orders) {
        List<ManagedOrderDto> managedOrderDtoList = new ArrayList<>();
        for (Order order : orders) {
            ManagedOrderDto managedOrderDto = new ManagedOrderDto();
            managedOrderDto.setId(order.getId());
            managedOrderDto.setAddress(order.getAddress());
            List<OrderItem> orderItems = order.getItemList();
            List<OrderItemDto> orderItemDtos = new ArrayList<>();
            for (OrderItem orderItem : orderItems) {
                orderItemDtos.add(new OrderItemDto(orderItem.getId(), orderItem.getProduct().getId(), orderItem.getAmount(), orderItem.getTotalPaymentEachOrderItem()));
            }
            managedOrderDto.setItemDtoList(orderItemDtos);
            managedOrderDto.setAddress(order.getAddress());
            managedOrderDto.setOrderStatus(order.getOrderStatus());
            managedOrderDto.setTotalPayment(order.getTotalPayment());
            //add to list
            managedOrderDtoList.add(managedOrderDto);
        }
        return managedOrderDtoList;
    }
}


