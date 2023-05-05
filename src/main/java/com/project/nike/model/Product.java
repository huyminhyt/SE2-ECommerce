package com.project.nike.model;

import com.project.nike.dto.CategoryDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String productName;
    @NotNull
    private String productDescription;
    @NotNull
    private String productImage;
    @NotNull
    private double productPrice;
    @NotNull
    private boolean productStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    public void map(Object o ){

    }


}
