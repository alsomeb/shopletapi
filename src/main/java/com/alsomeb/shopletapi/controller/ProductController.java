package com.alsomeb.shopletapi.controller;

import com.alsomeb.shopletapi.dto.ProductDto;

import com.alsomeb.shopletapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Set;

@RestController
@RequestMapping("api/v1/shoppinglists")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("{id}/products")
    public ResponseEntity<Set<ProductDto>> getProductsByShoppingListId(@PathVariable long id) {
        var products = productService.findAllProductsByShoppingListId(id);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
