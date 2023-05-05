package com.project.nike.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@lombok.Getter
@lombok.Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private List<ProductDto> listProductDto;
    private int pageNum;
    private int pageSize;
    private int sumPages;
    private boolean lastPage;
}
