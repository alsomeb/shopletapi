package com.alsomeb.shopletapi.service.impl;

import com.alsomeb.shopletapi.dto.ProductDto;

import com.alsomeb.shopletapi.entity.ProductEntity;

import com.alsomeb.shopletapi.repository.ProductRepository;

import com.alsomeb.shopletapi.service.ProductService;
import com.alsomeb.shopletapi.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ShoppingListService shoppingListService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ShoppingListService shoppingListService) {
        this.productRepository = productRepository;
        this.shoppingListService = shoppingListService;
    }

    @Override
    public Set<ProductDto> findAllProductsByShoppingListId(long id) {
        return shoppingListService.getById(id)
                .getProducts()
                .stream().map(productEntity -> toDTO(productEntity))
                .collect(Collectors.toSet());

    }


    private ProductEntity toEntity(ProductDto productDto) {
        return ProductEntity.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .amount(productDto.getAmount())
                .build();
    }

    private ProductDto toDTO(ProductEntity productEntity) {
        return ProductDto.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .amount(productEntity.getAmount())
                .build();
    }
}
