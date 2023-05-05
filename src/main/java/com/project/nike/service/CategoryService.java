package com.project.nike.service;


import com.project.nike.dto.CategoryDto;
import com.project.nike.dto.ProductByDto;
import com.project.nike.dto.SpecificCategoryDto;
import com.project.nike.model.Category;
import com.project.nike.model.Product;
import com.project.nike.repository.CategoryRepository;
import com.project.nike.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    public CategoryDto createNewCategory(CategoryDto categoryDto){
        Category category = this.modelMapper.map(categoryDto, Category.class);
        Category saveCategory = this.categoryRepository.save(category);
        return this.modelMapper.map(saveCategory, CategoryDto.class);
    }

    public List<CategoryDto> showCategoryList(){
        List<Category> categoryList =  categoryRepository.findAll();
        List<CategoryDto> categoryDtoList = categoryList.stream().map(category -> this.modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
        return categoryDtoList;
    }



    public SpecificCategoryDto showSpecificCategoryById(Long categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Product not found for id: " + categoryId));
        List<Product> productList = productRepository.findByCategoryId(categoryId);
        List<ProductByDto> productByDtoList = productList.stream().map(entity -> this.modelMapper.map(entity, ProductByDto.class)).collect(Collectors.toList());

        return new SpecificCategoryDto(category.getId(), category.getCategoryName(), category.getCategoryImage(), category.getCategoryDescription(), productByDtoList);
    }


    public CategoryDto updateCategoryById(CategoryDto newCategory, Long id) {
        Category category = this.categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found for id: " + id));
        category.setCategoryName(newCategory.getCategoryName());
        category.setCategoryImage(newCategory.getCategoryImage());
        category.setCategoryDescription(newCategory.getCategoryDescription());

        Category updatedCategory = this.categoryRepository.save(category);
        return this.modelMapper.map(updatedCategory, CategoryDto.class);

    }

    public void deleteCategory(Long id){
        Category category = this.categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found for id: " + id));
        this.categoryRepository.delete(category);
    }

}
