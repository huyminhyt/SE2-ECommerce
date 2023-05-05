package com.project.nike.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopInfoDto {
    public List<SimpleCategoryDto> categoryList;
    public List<ProductByDto> products;

}
