package com.alsomeb.shopletapi.controllers;

import com.alsomeb.shopletapi.dto.ShoppingListDto;
import com.alsomeb.shopletapi.entity.ShoppingListEntity;
import com.alsomeb.shopletapi.repository.ShoppingListRepository;
import com.alsomeb.shopletapi.service.ShoppingListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import static com.alsomeb.shopletapi.TestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// Help: https://www.arhohuttunen.com/spring-boot-integration-testing/
// Move Properties To a Profile With @ActiveProfiles, we run these tests with an H2 DB instead of PostGre SQL
// @Transactional // roll back any changes after tests
// @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) == CREATES/CLEAN DB Before Each Test

@SpringBootTest
@AutoConfigureMockMvc // will take care of creating the mock object for us
@ExtendWith(SpringExtension.class)
@ActiveProfiles("h2db")
public class ShoppingListEntityControllerIntegrationTest {

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

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    private final String apiRootURL = "/api/v1/shoppinglists";

    @Test
    public void testThatShoppingListIsCreated() throws Exception {
        final ShoppingListEntity shoppingListEntity = testShoppingListEntity();

        // In Order to get the JSON from the book we can use
        // Serialize dates: https://howtodoinjava.com/jackson/java-8-date-time-type-not-supported-by-default/
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Dependency for serializing LocalDateTime
        final String bookJSON = objectMapper.writeValueAsString(shoppingListEntity);

        // value() == Evaluate the JSON path expression against the response content and assert that the result is equal to the supplied value.
        mockMvc.perform(MockMvcRequestBuilders.post(apiRootURL)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.id").value(shoppingListEntity.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.description").value(shoppingListEntity.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.added").value(shoppingListEntity.getAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        // Creates a matcher that matches if the examined String contains the specified String anywhere.
        //.andExpect(content().string(containsString("api/shoppinglists/1")));

    }

    @Test
    public void testThatListShoppingListsReturns200EmptyWhenNothing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(apiRootURL))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testFindByIdReturn404IfShoppingListNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(apiRootURL.concat("/1")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByIdReturn200AndShoppingListIfExist() throws Exception {
        final ShoppingListDto shoppingListDto = testShoppingListDTO();
        var target = shoppingListService.save(shoppingListDto);

        mockMvc.perform(MockMvcRequestBuilders.get(apiRootURL.concat("/" + target.getId())))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.id").value(target.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.description").value(target.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.added").value(target.getAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
    }

    @Test
    public void testThatListShoppingListsReturns200AndListWhenExists() throws Exception {
        final ShoppingListDto shoppingListDto = testShoppingListDTO();
        var target = shoppingListService.save(shoppingListDto);

        mockMvc.perform(MockMvcRequestBuilders.get(apiRootURL))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        // $.[0].id == accesses the first element in the list
                        "$.[0].id").value(target.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.[0].description").value(target.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.[0].added").value(target.getAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
    }

    @Test
    public void testThatGetListsOrderByDateAscReturnsCorrect200() throws Exception {
        String requestURL = "/api/v1/shoppinglists/sort?order=asc";

        final ShoppingListDto targetFirst = ShoppingListDto.builder()
                .id(1L)
                .added(LocalDate.now())
                .description("First")
                .build();

        final ShoppingListDto targetLast = ShoppingListDto.builder()
                .id(2L)
                .added(LocalDate.now().plusDays(10))
                .description("Last")
                .build();

        final ShoppingListDto random = ShoppingListDto.builder()
                .id(2L)
                .added(LocalDate.now().plusDays(5))
                .description("random")
                .build();


        shoppingListService.save(targetLast);
        shoppingListService.save(random);
        shoppingListService.save(targetFirst);

        mockMvc.perform(MockMvcRequestBuilders.get(requestURL))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.[0].added").value(targetFirst.getAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.[2].added").value(targetLast.getAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

    }

    // Todo.. Delete, PUT

    @BeforeEach
    public void cleanDb() {
        // Annars får vi våran data.sql seed conflict med tests
        shoppingListRepository.deleteAll();
    }

}
