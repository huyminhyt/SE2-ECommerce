package com.project.nike.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleCategoryDto {
    private Long id;
    private String categoryName;
    private String categoryImage;
}
