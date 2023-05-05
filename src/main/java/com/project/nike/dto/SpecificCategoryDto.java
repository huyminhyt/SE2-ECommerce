package com.project.nike.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpecificCategoryDto {
    private Long id;
    private String categoryName;
    private String categoryImage;
    private String categoryDescription;
    private List<ProductByDto> product;
}
