package com.alsomeb.shopletapi;

import com.alsomeb.shopletapi.entity.ShoppingList;

import java.time.LocalDate;

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
}

