package com.project.nike.service;

import com.project.nike.dto.*;
import com.project.nike.model.Category;
import com.project.nike.model.Product;
import com.project.nike.repository.CategoryRepository;
import com.project.nike.repository.ProductRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private final ModelMapper modelMapper;


    public ProductService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    //create product
    public ProductDto createProduct(ProductDto productDto, Long categoryId){
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found for id: " + categoryId));

        //Dto -> entity
        Product product = toEntity(productDto);
        product.setCategory(category);
        Product saveProduct = productRepository.save(product);

        //entity -> dto
        ProductDto dto = toDto(saveProduct);
        return dto;
    }

    //get 8 products demo randomly
    public ShopInfoDto demoProducts(){
        List<Product> getProductList = productRepository.findAll();
        List<Category> getCategoryList = categoryRepository.findAll();

        Random random = new Random();
        List<ProductByDto> productLists = getProductList.stream().skip(random.nextInt(getProductList.size())).limit(8).map(entity -> this.modelMapper.map(entity, ProductByDto.class))
                .collect(Collectors.toList());
        List<SimpleCategoryDto> categoryDtoList = getCategoryList.stream().limit(6).map(category -> this.modelMapper.map(category, SimpleCategoryDto.class))
                .collect(Collectors.toList());

        return new ShopInfoDto(categoryDtoList, productLists);
    }

    //get all product
    public ProductResponse getAllProduct(int pageNum, int pageSize, String sortBy, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Product> page = this.productRepository.findAll(pageable);
        List<Product> productPage = page.getContent();
        List<ProductDto> dtoAll = productPage.stream().map(product -> this.toDto(product)).collect(Collectors.toList());

        ProductResponse response = new ProductResponse();
        response.setListProductDto(dtoAll);
        response.setPageNum(page.getNumber());
        response.setPageSize(page.getSize());
        response.setSumPages(page.getTotalPages());
        response.setLastPage(page.isLast());

        return response;
    }

    //get product by id
    public ProductDto getProductById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found for id: " + id));

        ProductDto productDto = this.toDto(product);
        return productDto;
    }

    //update product

    public ProductDto updateProduct(Long id, ProductDto newProduct, Long categoryId){
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found for id: " + categoryId));
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found for id: " + id));
        product.setProductName(newProduct.getProductName());
        product.setProductDescription(newProduct.getProductDescription());
        product.setProductImage(newProduct.getProductImage());
        product.setProductPrice(newProduct.getProductPrice());
        product.setProductStatus(newProduct.isProductStatus());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        ProductDto productDtoUpdated = toDto(updatedProduct);
        return productDtoUpdated;
    }

    //delete product

    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }



    //DTO -> ENTITY
    public Product toEntity(ProductDto productDto){
        Product product = new Product();
        product.setId(productDto.getId());
        product.setProductName(productDto.getProductName());
        product.setProductDescription(productDto.getProductDescription());
        product.setProductImage(productDto.getProductImage());
        product.setProductPrice(productDto.getProductPrice());
        product.setProductStatus(productDto.isProductStatus());

        return product;
    }

    //ENTITY -> DTO
    public ProductDto toDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setProductName(product.getProductName());
        productDto.setProductDescription(product.getProductDescription());
        productDto.setProductImage(product.getProductImage());
        productDto.setProductPrice(product.getProductPrice());
        productDto.setProductStatus(product.isProductStatus());

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(product.getCategory().getId());
        categoryDto.setCategoryName(product.getCategory().getCategoryName());
        categoryDto.setCategoryImage(product.getCategory().getCategoryImage());
        categoryDto.setCategoryDescription(product.getCategory().getCategoryDescription());

        productDto.setCategory(categoryDto);

        return productDto;
    }

}
