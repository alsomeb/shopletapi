package com.alsomeb.shopletapi;

import com.alsomeb.shopletapi.dto.ProductDto;
import com.alsomeb.shopletapi.dto.ShoppingListDto;
import com.alsomeb.shopletapi.entity.ProductEntity;
import com.alsomeb.shopletapi.entity.ShoppingListEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

// Final, cannot be extended and private constructor
// We only use static method
public final class TestData {

    private TestData() {}

    public static ShoppingListEntity testShoppingListEntity() {
        return ShoppingListEntity.builder()
                .id(1L)
                .added(LocalDate.now())
                .description("Test")
                .build();
    }

    public static ShoppingListDto testShoppingListDTO() {
        return ShoppingListDto.builder()
                .id(1L)
                .added(LocalDate.now())
                .description("Test")
                .build();
    }

    public static ProductDto testProductDTO() {
        return ProductDto.builder()
                .id(1L)
                .amount(5)
                .name("test product")
                .build();
    }

    public static ProductEntity testProductEntity() {
        return ProductEntity.builder()
                .id(1L)
                .amount(5)
                .name("test product")
                .shoppingList(testShoppingListEntity())
                .build();
    }

    public static Set<ProductEntity> testProductEntitySet() {
        return Set.of(
                ProductEntity.builder()
                        .id(1L)
                        .amount(5)
                        .name("test product 1")
                        .shoppingList(testShoppingListEntity())
                        .build(),

                ProductEntity.builder()
                        .id(2L)
                        .amount(10)
                        .name("test product 2")
                        .shoppingList(testShoppingListEntity())
                        .build(),

                ProductEntity.builder()
                        .id(3L)
                        .amount(15)
                        .name("test product 3")
                        .shoppingList(testShoppingListEntity())
                        .build()
        );
    }

    public static Set<ProductDto> testProductDTOSet() {
        return Set.of(
                ProductDto.builder()
                        .id(1L)
                        .amount(5)
                        .name("test product 1")
                        .build(),

                ProductDto.builder()
                        .id(2L)
                        .amount(10)
                        .name("test product 2")
                        .build(),

                ProductDto.builder()
                        .id(3L)
                        .amount(15)
                        .name("test product 3")
                        .build()
        );
    }

    public static List<ShoppingListEntity> listOfShoppingLists() {
        return List.of(
                ShoppingListEntity.builder()
                    .id(1L)
                    .added(LocalDate.now())
                    .description("Test 1")
                    .build(),

                ShoppingListEntity.builder()
                    .id(2L)
                    .added(LocalDate.now().plusDays(5))
                    .description("Test 2")
                    .build(),

                ShoppingListEntity.builder()
                    .id(3L)
                    .added(LocalDate.now().plusDays(10))
                    .description("Test 3")
                    .build());

    }

    public static List<ShoppingListDto> listOfShoppingListsDTO() {
        return List.of(
                ShoppingListDto.builder()
                        .id(1L)
                        .added(LocalDate.now())
                        .description("Test 1")
                        .build(),

                ShoppingListDto.builder()
                        .id(2L)
                        .added(LocalDate.now().plusDays(5))
                        .description("Test 2")
                        .build(),

                ShoppingListDto.builder()
                        .id(3L)
                        .added(LocalDate.now().plusDays(10))
                        .description("Test 3")
                        .build());

    }
}

