package com.project.nike.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductByDto {
    private Long id;
    private String productName;
    private String productImage;
    private Double productPrice;
}
