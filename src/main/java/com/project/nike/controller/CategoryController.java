package com.project.nike.controller;


import com.project.nike.dto.CategoryDto;
import com.project.nike.repository.CategoryRepository;
import com.project.nike.response.ResponseHandler;
import com.project.nike.service.CategoryService;
import com.project.nike.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;



    @GetMapping("/")
    public ResponseEntity<Object> showAllCategory(){
        return ResponseHandler.responseEntity("success", HttpStatus.ACCEPTED, this.categoryService.showCategoryList());
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Object> showCategoryById(@PathVariable Long categoryId){
        return ResponseHandler.responseEntity("success", HttpStatus.ACCEPTED, this.categoryService.showSpecificCategoryById(categoryId));
    }

    @PostMapping("/admin/add")
    public ResponseEntity<Object> createNewCategory(@RequestBody CategoryDto categoryDto){
        return ResponseHandler.responseEntity("success", HttpStatus.CREATED, this.categoryService.createNewCategory(categoryDto));
    }

    @PutMapping("/admin/update/{categoryId}")
    public ResponseEntity<Object> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable Long categoryId){
        return ResponseHandler.responseEntity("success", HttpStatus.ACCEPTED, this.categoryService.updateCategoryById(categoryDto, categoryId));
    }

    @DeleteMapping("/admin/delete/{categoryId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long categoryId){
        this.categoryService.deleteCategory(categoryId);
        return  ResponseHandler.responseEntity("success", HttpStatus.ACCEPTED, null );
    }
}
