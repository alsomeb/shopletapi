package com.alsomeb.shopletapi.services.impl;

import com.alsomeb.shopletapi.dto.ProductDto;
import com.alsomeb.shopletapi.entity.ProductEntity;
import com.alsomeb.shopletapi.entity.ShoppingListEntity;
import com.alsomeb.shopletapi.repository.ProductRepository;
import com.alsomeb.shopletapi.repository.ShoppingListRepository;
import com.alsomeb.shopletapi.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static com.alsomeb.shopletapi.TestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    ShoppingListRepository shoppingListRepository;

    @InjectMocks
    private ProductServiceImpl underTest;

    @Test
    public void testThatProductIsSavedToShoppingList() {
        ShoppingListEntity shoppingListEntity = testShoppingListEntity();
        shoppingListEntity.setProducts(Set.of(testProductEntity()));

        ProductDto productDto = testProductDTO();
        ProductEntity productEntity = testProductEntity();

        when(shoppingListRepository.findById(shoppingListEntity.getId())).thenReturn(Optional.of(shoppingListEntity));
        when(productRepository.save(productEntity)).thenReturn(productEntity);

        var result = underTest.saveProductToShoppingList(shoppingListEntity.getId(), productDto);

        assertThat(shoppingListEntity.getProducts())
                .hasSize(1)
                .extracting(ProductEntity::getId)
                .contains(result.getId());

        assertThat(result)
                .isEqualTo(productDto);


    }
}
