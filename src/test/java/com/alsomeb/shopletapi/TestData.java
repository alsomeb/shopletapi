package com.alsomeb.shopletapi;

import com.alsomeb.shopletapi.dto.ShoppingListDto;
import com.alsomeb.shopletapi.entity.ShoppingListEntity;

import java.time.LocalDate;
import java.util.List;

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
}

