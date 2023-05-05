package com.project.nike.controller;


import com.project.nike.dto.AppConstants;
import com.project.nike.dto.ProductDto;
import com.project.nike.repository.CategoryRepository;
import com.project.nike.repository.ProductRepository;
import com.project.nike.response.ResponseHandler;
import com.project.nike.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/main-shop")
    public ResponseEntity<Object> showMainShop(){
        return ResponseHandler.responseEntity("success", HttpStatus.ACCEPTED, productService.demoProducts());
    }


    //show all product
    @GetMapping("/")
    public ResponseEntity<Object> show(@RequestParam(value = "pageNumber") int pageNumber,
                                               @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
                                               @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_ID, required = false) String sortBy,
                                               @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir ) {
        return ResponseHandler.responseEntity("success", HttpStatus.ACCEPTED, productService.getAllProduct(pageNumber, pageSize, sortBy, sortDir));
    }






    //show product by id
    @GetMapping("/{productId}")
    public ResponseEntity<Object> showProductById(@PathVariable Long productId){
        return ResponseHandler.responseEntity("success", HttpStatus.ACCEPTED, productService.getProductById(productId));
    }

    //Add product
    @PostMapping("/admin/add")
    @ResponseBody
    public ResponseEntity<Object> addProduct(@RequestBody ProductDto productDto,
                                             @RequestParam(value = "cateId") Long categoryId,
                                             BindingResult newProduct
                                             ) throws Exception {
        if(newProduct.hasErrors()){
            return ResponseHandler.errorResponseEntity("failure", HttpStatus.NOT_FOUND, "Product already existed");
        } else {
            return ResponseHandler.responseEntity("success", HttpStatus.CREATED, productService.createProduct(productDto, categoryId));
        }
    }

    //Update product
    @PutMapping("/admin/update/{productId}/")
    public ResponseEntity<Object> updateProduct(@PathVariable Long productId,
                                                @RequestBody ProductDto productDto,
                                                @RequestParam(value = "cateId") Long categoryId
                                                ) {
        return ResponseHandler.responseEntity("sucess", HttpStatus.ACCEPTED, productService.updateProduct(productId, productDto, categoryId));
    }

    //Delete product
    @DeleteMapping("/admin/delete/{productId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return ResponseHandler.responseEntity("success", HttpStatus.ACCEPTED, null);
    }

}
