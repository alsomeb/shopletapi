package com.alsomeb.shopletapi;

import com.alsomeb.shopletapi.entity.ShoppingList;

import java.time.LocalDate;
import java.util.List;

// Final, cannot be extended and private constructor
// We only use static method
public final class TestData {

    private TestData() {}

    public static ShoppingList testShoppingListEntity() {
        return ShoppingList.builder()
                .id(1L)
                .added(LocalDate.now())
                .description("Test")
                .build();
    }

    public static List<ShoppingList> listOfShoppingLists() {
        return List.of(
                ShoppingList.builder()
                    .id(1L)
                    .added(LocalDate.now())
                    .description("Test 1")
                    .build(),

                ShoppingList.builder()
                    .id(2L)
                    .added(LocalDate.now())
                    .description("Test 2")
                    .build(),

                ShoppingList.builder()
                    .id(3L)
                    .added(LocalDate.now())
                    .description("Test 3")
                    .build());

    }
}

