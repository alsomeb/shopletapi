package com.alsomeb.shopletapi;

import com.alsomeb.shopletapi.entity.ShoppingList;
import com.alsomeb.shopletapi.exception.ShoppingListNotFoundException;
import com.alsomeb.shopletapi.repository.ShoppingListRepository;
import com.alsomeb.shopletapi.service.ShoppingListServiceImpl;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static com.alsomeb.shopletapi.TestData.listOfShoppingLists;
import static com.alsomeb.shopletapi.TestData.testShoppingListEntity;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


// Guide, still learning Mocking
// https://www.youtube.com/watch?v=HmRVrAT4uA0&list=PLMVHTRBusikoEW-dVLcBJrdGQ3A9Eydj_&index=10&t=3083s

@ExtendWith(MockitoExtension.class)
class ShoppingListImplTest {

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
        ShoppingList shoppingList = testShoppingListEntity();

        ShoppingList expectedList = ShoppingList.builder()
                .id(1L)
                .added(LocalDate.now())
                .description("Test")
                .build();

        when(shoppingListRepository.save(eq(shoppingList))).thenReturn(shoppingList);

        final ShoppingList result = underTest.save(shoppingList);
        assertThat(result)
                .isEqualTo(expectedList);
    }

    @Test
    public void testFindListByIdThrowsExceptionIfNoneFind() {
        final long targetId = 1L;
        when(shoppingListRepository.findById(eq(targetId))).thenThrow(ShoppingListNotFoundException.class);
        assertThatThrownBy(() -> underTest.getById(targetId))
                .isInstanceOf(ShoppingListNotFoundException.class);
    }

    @Test
    public void testFindListByIdReturnsCorrectBookIfExists() {
        final long targetId = 1L;

        // Optional.Of() == Returns an Optional describing the given non-null value
        when(shoppingListRepository.findById(eq(targetId))).thenReturn(Optional.of(testShoppingListEntity()));

        final ShoppingList result = underTest.getById(targetId);
        final ShoppingList expected = ShoppingList.builder()
                .id(targetId)
                .description("Test")
                .added(LocalDate.now())
                .build();

        assertThat(result)
                .isNotNull()
                .isEqualTo(expected);

    }

    @Test
    public void testFindAllShoppingListsReturnsEmptyListWhenNone() {
        // Mock repository to empty list on return
        when(shoppingListRepository.findAll()).thenReturn(new ArrayList<>());
        final var result = underTest.getAllShoppingLists();

        assertThat(result)
                .hasSize(0);
    }

    @Test
    public void testFindAllShoppingListsReturnsListWhenNotEmpty() {
        final List<ShoppingList> expected = listOfShoppingLists();

        // Mock Repository results with expected list above
        when(shoppingListRepository.findAll()).thenReturn(expected);
        final var result = underTest.getAllShoppingLists();

        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);
    }

}
