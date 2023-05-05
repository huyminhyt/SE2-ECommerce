package com.project.nike.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private long id;
    private String productName;
    private String productDescription;
    private String productImage;
    private double productPrice;
    private boolean productStatus;
    private CategoryDto category;
}
