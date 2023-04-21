package com.alsomeb.shopletapi.services.impl;

import com.alsomeb.shopletapi.dto.ShoppingListDto;
import com.alsomeb.shopletapi.entity.ShoppingListEntity;
import com.alsomeb.shopletapi.exception.ShoppingListNotFoundException;
import com.alsomeb.shopletapi.repository.ShoppingListRepository;
import com.alsomeb.shopletapi.service.impl.ShoppingListServiceImpl;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static com.alsomeb.shopletapi.TestData.*;
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
class ShoppingListEntityImplTest {

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
        ShoppingListEntity shoppingListEntity = testShoppingListEntity();
        ShoppingListDto shoppingListDto = testShoppingListDTO();

        when(shoppingListRepository.save(eq(shoppingListEntity))).thenReturn(shoppingListEntity);

        final ShoppingListDto result = underTest.save(shoppingListDto);
        assertThat(result)
                .isEqualTo(shoppingListDto);
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

        final ShoppingListDto result = underTest.getById(targetId);
        final ShoppingListDto expected = ShoppingListDto.builder()
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
        final List<ShoppingListEntity> expected = listOfShoppingLists();

        // Mock Repository results with expected list above
        when(shoppingListRepository.findAll()).thenReturn(expected);
        final var result = underTest.getAllShoppingLists();

        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);
    }

    // TODO DeleteById

    @Test
    public void testThatGetListsOrderByDateAscReturnsCorrect() {
        List<ShoppingListDto> testListDTO = listOfShoppingListsDTO();
        List<ShoppingListEntity> mockListEntity = listOfShoppingLists();

        when(shoppingListRepository.findAll()).thenReturn(mockListEntity);
        final var result = underTest.getListsOrderByDateAsc();

        assertThat(result)
                .isNotNull()
                .isNotEmpty();

        assertThat(result.get(0))
                .isEqualTo(testListDTO.get(0));
    }

}
