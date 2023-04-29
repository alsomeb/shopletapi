package com.alsomeb.shopletapi.services.impl;

import com.alsomeb.shopletapi.dto.ProductDto;
import com.alsomeb.shopletapi.entity.ProductEntity;
import com.alsomeb.shopletapi.entity.ShoppingListEntity;
import com.alsomeb.shopletapi.exception.ProductNotFoundException;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ShoppingListRepository shoppingListRepository;

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

    @Test
    public void testThatProductIsDeletedFromList() {
        ProductEntity productEntity = testProductEntity();

        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        var result = underTest.deleteProductById(productEntity.getId());
        verify(productRepository, times(1)).findById(eq(productEntity.getId()));

        assertThat(result.deleted())
                .isEqualTo(true);
    }

    @Test
    public void testThatFindProductByIdThrowsNoSuchProductExceptionIfNotFound() {
        ProductEntity productEntity = testProductEntity();

        assertThatThrownBy(() -> underTest.findProductById(productEntity.getId()))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    public void testThatFindProductByIdReturnsProductIfFound() {
        ProductEntity productEntity = testProductEntity();
        ProductDto productDto = testProductDTO();
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));

        var result = underTest.findProductById(productDto.getId());

        ProductDto nope = ProductDto.builder()
                .id(232L)
                .name("Nope")
                .amount(25152)
                .build();

        assertThat(result)
                .isNotEqualTo(nope)
                .isEqualTo(productDto);
    }
}
