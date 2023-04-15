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


// Guide, still learning Mocking
// https://www.youtube.com/watch?v=HmRVrAT4uA0&list=PLMVHTRBusikoEW-dVLcBJrdGQ3A9Eydj_&index=10&t=3083s

@ExtendWith(MockitoExtension.class)
class ShopletapiApplicationTests {

    // @Mock creates a mock repository and its behaviour using when() and thenReturn() method.

    // The tested class should be annotated with "InjectMocks", this tells Mockito which class to inject mocks into.
    // @InjectMock creates an instance of the class and injects the mocks that are marked with the annotations @Mock into it.
    @Mock
    private ShoppingListRepository shoppingListRepository;

    // this will be injected into shoppingListRepository
    @InjectMocks
    private ShoppingListServiceImpl underTest;

    @Test // 35.58 in video
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
