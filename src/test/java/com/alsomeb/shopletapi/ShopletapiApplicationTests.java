package com.alsomeb.shopletapi;

import com.alsomeb.shopletapi.entity.ShoppingList;
import com.alsomeb.shopletapi.repository.ShoppingListRepository;
import com.alsomeb.shopletapi.service.ShoppingListService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class ShopletapiApplicationTests {


    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private ShoppingListService shoppingListService;

    @Test
    public void testSortOrderDescWorksAsIntended() {
        // Given
        var testList = List.of(
                ShoppingList.builder()
                        .id(1L)
                        .added(LocalDate.now().plusDays(50))
                        .description("Test 1")
                        .build(),

                ShoppingList.builder()
                        .id(2L)
                        .added(LocalDate.now().plusDays(20))
                        .description("Test 2")
                        .build(),

                ShoppingList.builder()
                        .id(3L)
                        .added(LocalDate.now().plusDays(10))
                        .description("Test 3")
                        .build()
        );

        shoppingListRepository.saveAll(testList);

        // when
        var resultList = shoppingListService.getListsOrderByDateDesc();
        LocalDate targetDateAsc1 = resultList.get(0).getAdded();
        LocalDate targetDateAsc2 = resultList.get(1).getAdded();
        LocalDate targetDateAsc3 = resultList.get(2).getAdded();
        System.out.println(resultList);

        // then
        assertThat(resultList)
                .hasSize(3)
                .doesNotContainNull();

        assertThat(resultList.get(0))
                .extracting(ShoppingList::getAdded)
                .isEqualTo(targetDateAsc1);

        assertThat(resultList.get(1))
                .extracting(ShoppingList::getAdded)
                .isEqualTo(targetDateAsc2);

        assertThat(resultList.get(2))
                .extracting(ShoppingList::getAdded)
                .isEqualTo(targetDateAsc3);

    }

}
