package com.alsomeb.shopletapi;

import com.alsomeb.shopletapi.entity.ShoppingList;
import com.alsomeb.shopletapi.service.ShoppingListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.format.DateTimeFormatter;

import static com.alsomeb.shopletapi.TestData.testShoppingListEntity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc // will take care of creating the mock object for us
@ExtendWith(SpringExtension.class)
public class ShoppingListControllerIntegrationTest {

    // MockMVC allows us to test the API as if we were calling it
    // We starts up the entire application and hitting the rest api endpoints
    // and making sure what is returned from endpoint is correct

    /*
    If you want check json Key and value you can use jsonpath
    .andExpect(jsonPath("$.yourKeyValue", is/value("WhatYouExpect")));

     */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShoppingListService shoppingListService;

    private final String apiRootURL = "/api/shoppinglists";

    @Test
    public void testThatShoppingListIsCreated() throws Exception {
        final ShoppingList shoppingList = testShoppingListEntity();

        // In Order to get the JSON from the book we can use
        // Serialize dates: https://howtodoinjava.com/jackson/java-8-date-time-type-not-supported-by-default/
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Dependency for serializing LocalDateTime
        final String bookJSON = objectMapper.writeValueAsString(shoppingList);

        // value() == Evaluate the JSON path expression against the response content and assert that the result is equal to the supplied value.
        mockMvc.perform(MockMvcRequestBuilders.post(apiRootURL)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.id").value(shoppingList.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.description").value(shoppingList.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.added").value(shoppingList.getAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        // Creates a matcher that matches if the examined String contains the specified String anywhere.
        //.andExpect(content().string(containsString("api/shoppinglists/1")));

    }

    @Test
    public void testFindByIdReturn404IfShoppingListNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(apiRootURL.concat("/1")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByIdReturn200AndShoppingListIfExist() throws Exception {
        final ShoppingList testList = testShoppingListEntity();
        shoppingListService.save(testList);

        mockMvc.perform(MockMvcRequestBuilders.get(apiRootURL.concat("/" + testList.getId())))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.id").value(testList.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.description").value(testList.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.added").value(testList.getAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
    }
}
