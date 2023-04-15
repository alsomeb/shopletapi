package com.alsomeb.shopletapi;

import com.alsomeb.shopletapi.entity.ShoppingList;
import com.alsomeb.shopletapi.repository.ShoppingListRepository;
import com.alsomeb.shopletapi.service.ShoppingListServiceImpl;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;


// Guide for this
// https://www.youtube.com/watch?v=HmRVrAT4uA0&list=PLMVHTRBusikoEW-dVLcBJrdGQ3A9Eydj_&index=10&t=3083s

@ExtendWith(MockitoExtension.class)
class ShopletapiApplicationTests {

    // Only Unit test, mock
    @Mock
    private ShoppingListRepository shoppingListRepository;

    @InjectMocks
    private ShoppingListServiceImpl underTest;

    @Test // 35.58
    public void testShoppingListIsSaved() {
        ShoppingList shoppingList = ShoppingList.builder()
                .id(1L)
                .added(LocalDate.now())
                .description("Alex")
                .build();

        ShoppingList expectedList = ShoppingList.builder()
                .id(1L)
                .added(LocalDate.now())
                .description("Alex")
                .build();

        when(shoppingListRepository.save(eq(shoppingList))).thenReturn(shoppingList);

        final ShoppingList result = underTest.save(shoppingList);
        assertThat(result)
                .isEqualTo(expectedList);
    }

}
