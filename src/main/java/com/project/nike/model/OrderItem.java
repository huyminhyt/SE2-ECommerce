package com.project.nike.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;
    private int amount;
    private double totalPaymentEachOrderItem;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
