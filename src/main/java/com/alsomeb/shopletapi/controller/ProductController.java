package com.alsomeb.shopletapi.controller;

import com.alsomeb.shopletapi.dto.ProductDto;

import com.alsomeb.shopletapi.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Set;

@RestController
@RequestMapping("api/v1")
@Slf4j
public class ProductController {

    private final ProductService productService;


    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("shoppinglists/{id}/products")
    public ResponseEntity<Set<ProductDto>> getProductsByShoppingListId(@PathVariable long id) {
        var products = productService.findAllProductsByShoppingListId(id);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("shoppinglists/{id}/products")
    public ResponseEntity<ProductDto> saveProductToList(@PathVariable long id, @Valid @RequestBody ProductDto productDto) {
        var savedProduct = productService.saveProductToShoppingList(id, productDto);
        log.info("Added product with name: {} to shoppinglist with id: {}", savedProduct.getName(), id);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
}
