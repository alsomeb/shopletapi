package com.alsomeb.shopletapi.service.impl;

import com.alsomeb.shopletapi.dto.ProductDto;

import com.alsomeb.shopletapi.entity.ProductEntity;

import com.alsomeb.shopletapi.entity.ShoppingListEntity;
import com.alsomeb.shopletapi.exception.ProductNotFoundException;
import com.alsomeb.shopletapi.exception.ShoppingListNotFoundException;
import com.alsomeb.shopletapi.repository.ProductRepository;

import com.alsomeb.shopletapi.repository.ShoppingListRepository;
import com.alsomeb.shopletapi.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ShoppingListRepository shoppingListRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ShoppingListRepository shoppingListRepository) {
        this.productRepository = productRepository;
        this.shoppingListRepository = shoppingListRepository;
    }

    @Override
    public Set<ProductDto> findAllProductsByShoppingListId(long id) {
        Set<ProductEntity> productsEntities = shoppingListRepository.findById(id)
                .map(ShoppingListEntity::getProducts)
                .orElseThrow(() -> new ShoppingListNotFoundException("Cant find ShoppingList with id: " + id));

        return toDTOSet(productsEntities);
    }

    @Override
    public ProductDto saveProductToShoppingList(Long listId, ProductDto productDto) {
        ShoppingListEntity shoppingListEntity = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new ShoppingListNotFoundException("Cant find ShoppingList with id: " + listId));

        // Apply Shopping list with given ID to the product posted and save it to db
        ProductEntity productEntity = toEntity(productDto);
        productEntity.setShoppingList(shoppingListEntity);
        ProductEntity savedEntity = productRepository.save(productEntity);

        return toDTO(savedEntity);
    }

    @Override
    public ProductDto findProductById(long id) {
        return productRepository.findById(id).stream()
                .map(productEntity -> toDTO(productEntity))
                .findFirst().orElseThrow(() -> new ProductNotFoundException("Cant find Product with id: " + id));
    }


    // Helper Methods
    private ProductEntity toEntity(ProductDto productDto) {
        return ProductEntity.builder()
                .id(productDto.getId())
                .name(productDto.getName().toLowerCase())
                .amount(productDto.getAmount())
                .build();
    }

    private ProductDto toDTO(ProductEntity productEntity) {
        return ProductDto.builder()
                .id(productEntity.getId())
                .name(productEntity.getName().toLowerCase())
                .amount(productEntity.getAmount())
                .build();
    }

    private Set<ProductDto> toDTOSet(Set<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(productEntity -> toDTO(productEntity))
                .collect(Collectors.toSet());
    }
}
