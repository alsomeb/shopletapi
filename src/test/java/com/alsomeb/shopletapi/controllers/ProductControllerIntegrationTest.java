package com.alsomeb.shopletapi.controllers;

import com.alsomeb.shopletapi.dto.ProductDto;
import com.alsomeb.shopletapi.entity.ProductEntity;
import com.alsomeb.shopletapi.entity.ShoppingListEntity;
import com.alsomeb.shopletapi.repository.ProductRepository;
import com.alsomeb.shopletapi.repository.ShoppingListRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


import static com.alsomeb.shopletapi.TestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ShoppingListRepository shoppingListRepository;

    /*
    @Value("${token}")
    private String token;

     */


    // Här mockar vi datan ist

    @Test
    public void testThatProductIsCreatedReturns201() throws Exception {
        ProductEntity productToSave = testProductEntity();
        ShoppingListEntity shoppingListEntity = testShoppingListEntity();

        when(productRepository.save(productToSave)).thenReturn(productToSave);
        when(shoppingListRepository.findById(shoppingListEntity.getId())).thenReturn(Optional.of(shoppingListEntity));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String productJSON = mapper.writeValueAsString(productToSave);

        String url = "/api/v1/shoppinglists/" + shoppingListEntity.getId() + "/products";

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(productJSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.id").value(productToSave.getId())
                )
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.name").value(productToSave.getName())
                )
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.amount").value(productToSave.getAmount()));

        // Verifies we actually called these methods in their repos
        verify(shoppingListRepository, times(1)).findById(shoppingListEntity.getId());
        verify(productRepository, times(1)).save(productToSave);
    }

    @Test
    public void testThatProductIsUpdatedReturns200() throws Exception {
        ProductEntity productToUpdate = testProductEntity();

        when(productRepository.save(productToUpdate)).thenReturn(productToUpdate);
        when(productRepository.findById(productToUpdate.getId())).thenReturn(Optional.of(productToUpdate));

        productToUpdate.setName("updated");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String productJSON = mapper.writeValueAsString(productToUpdate);

        String url = "/api/v1/products/" + testProductEntity().getId();

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.id").value(productToUpdate.getId())
                )
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.name").value(productToUpdate.getName())
                )
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.amount").value(productToUpdate.getAmount()));

        verify(productRepository, times(1)).findById(productToUpdate.getId());
        verify(productRepository, times(1)).save(productToUpdate);
    }

    @Test
    public void testFindProductByIdReturnsCorrectProductAnd200() throws Exception {
        String url = "/api/v1/products/" + testProductEntity().getId();
        ProductEntity foundProduct = testProductEntity();
        when(productRepository.findById(foundProduct.getId())).thenReturn(Optional.of(foundProduct));

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.id").value(foundProduct.getId())
                )
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.name").value(foundProduct.getName())
                )
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.amount").value(foundProduct.getAmount()));

        verify(productRepository, times(1)).findById(foundProduct.getId());
    }

    @Test
    public void testFindProductByIdReturns404IfNotFound() throws Exception {
        long productId = 1337L;
        String url = "/api/v1/products/" + productId;
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.message").value("Cant find Product with id: 1337"));

        verify(productRepository, times(1)).findById(productId);

    }

    @Test
    public void testFindAllProductsByShoppingListIdReturnsCorrectListIfListAndProductsExists() throws Exception {

        Set<ProductEntity> productSet = testProductEntitySet();
        Set<ProductDto> expectedList = testProductDTOSet(); // Används för att skriva till JSON lista för att jämföra resultat i test

        // ShoppingListan har inga produkter fäst på objektet från början dvs null, nedan har vi ett Set<ProductEntity>
        // som vi fäster på Shoppinglistan annars kommer vi få 404 så som vi byggt i service
        ShoppingListEntity shoppingListEntity = testShoppingListEntity();
        shoppingListEntity.setProducts(productSet);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Dependency for serializing LocalDateTime
        String productSetJSON = mapper.writeValueAsString(expectedList);

        when(shoppingListRepository.findById(shoppingListEntity.getId())).thenReturn(Optional.of(shoppingListEntity));

        String url = "/api/v1/shoppinglists/" + shoppingListEntity.getId() + "/products";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(productSetJSON));

        verify(shoppingListRepository, times(1)).findById(shoppingListEntity.getId());

    }

    @Test
    public void testFindAllProductsByShoppingListIdReturnsEmptyListIfNoProducts() throws Exception {
        ShoppingListEntity shoppingListEntity = testShoppingListEntity();
        shoppingListEntity.setProducts(new HashSet<>()); // Tomt Set


        when(shoppingListRepository.findById(any())).thenReturn(Optional.of(shoppingListEntity)); // any() == Matches anything, vilket ID som helst

        String url = "/api/v1/shoppinglists/" + shoppingListEntity.getId() + "/products";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        verify(shoppingListRepository, times(1)).findById(shoppingListEntity.getId());

    }

    @Test
    public void testDeleteProductByIdReturnsTrueIfExists() throws Exception {
        ProductEntity foundProduct = testProductEntity();
        String url = "/api/v1/products/" + foundProduct.getId();
        when(productRepository.findById(foundProduct.getId())).thenReturn(Optional.of(foundProduct));

        mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.deleted").value(true));

        verify(productRepository, times(1)).findById(foundProduct.getId());
    }

    @Test
    public void testDeleteProductByIdReturnsFalseIfDoesntExists() throws Exception {
        long productId = 1337L;
        String url = "/api/v1/products/" + productId;

        mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.deleted").value(false));

        verify(productRepository, times(1)).findById(productId);
    }

    /*
    @Test
    public void testThatDeleteNotPossibleWithNoToken() throws Exception {
        long productId = 1337L;
        String url = "/api/v1/products/" + productId;
        mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testFindAllProductsByShoppingListIdNotPossibleWithoutToken() throws Exception {
        ShoppingListEntity shoppingListEntity = testShoppingListEntity();
        String url = "/api/v1/shoppinglists/" + shoppingListEntity.getId() + "/products";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testFindProductByIdNotPossibleWithoutToken() throws Exception {
        long productId = 1337L;
        String url = "/api/v1/products/" + productId;
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testThatProductIsUpdatedNotPossibleWithoutToken() throws Exception {
        String url = "/api/v1/products/" + testProductEntity().getId();

        mockMvc.perform(MockMvcRequestBuilders.put(url))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testThatProductIsSavedNotPossibleWithoutToken() throws Exception {
        String url = "/api/v1/products/" + testProductEntity().getId();

        mockMvc.perform(MockMvcRequestBuilders.post(url))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
     */

}
