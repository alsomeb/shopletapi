package com.alsomeb.shopletapi.controllers;

import com.alsomeb.shopletapi.entity.ProductEntity;
import com.alsomeb.shopletapi.repository.ProductRepository;
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

import static com.alsomeb.shopletapi.TestData.testProductEntity;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2db")
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private ProductRepository productRepository;


    // Här mockar vi datan ist

    @Test
    public void testThatProductIsCreated201() throws Exception {
        ProductEntity productToSave = testProductEntity();

        when(productRepository.save(productToSave)).thenReturn(productToSave);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String productJSON = mapper.writeValueAsString(productToSave);

        String url = "/api/v1/shoppinglists/10001/products";

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
    }

}
