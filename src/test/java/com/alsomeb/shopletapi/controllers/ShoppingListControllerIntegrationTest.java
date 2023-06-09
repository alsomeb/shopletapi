package com.alsomeb.shopletapi.controllers;

import com.alsomeb.shopletapi.dto.ShoppingListDto;
import com.alsomeb.shopletapi.repository.ShoppingListRepository;
import com.alsomeb.shopletapi.service.ShoppingListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import static com.alsomeb.shopletapi.TestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Help: https://www.arhohuttunen.com/spring-boot-integration-testing/
// Move Properties To a Profile With @ActiveProfiles, we run these tests with an H2 DB instead of PostGre SQL
// MockMVC allows us to test the API as if we were calling it
// We starts up the entire application and hitting the rest api endpoints
// and making sure what is returned from endpoint is correct

    /*
    If you want check json Key and value you can use jsonpath
    .andExpect(jsonPath("$.yourKeyValue", is/value("WhatYouExpect")));
     */

@SpringBootTest
@AutoConfigureMockMvc // will take care of creating the mock object for us
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // CREATES/CLEAN DB Before Each Test, startar om context för varje test
public class ShoppingListControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShoppingListService shoppingListService;

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    /*
    @Value("${token}")
    private String token;

     */

    private final String apiRootURL = "/api/v1/shoppinglists";

    // ANVÄNDER EJ MOCKING I DESSA TESTER, IMPL I ProductControllerIntegrationTest

    @Test
    public void testThatShoppingListIsCreated201() throws Exception {
        // Blir utan ID här men vi får id när vi .save() mha Service
        var dto = ShoppingListDto.builder()
                .id(1L)
                .added(LocalDate.now())
                .description("Test")
                .build();

        // In Order to get the JSON from the shoppinglist we can use
        // Serialize dates: https://howtodoinjava.com/jackson/java-8-date-time-type-not-supported-by-default/
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Dependency for serializing LocalDateTime
        final String listJSON = objectMapper.writeValueAsString(dto);

        // value() == Evaluate the JSON path expression against the response content and assert that the result is equal to the supplied value.
        // Vi anv put metoden för att skapa / upd
        mockMvc.perform(MockMvcRequestBuilders.put(apiRootURL + "/" + dto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                        .content(listJSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.id").value(dto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.description").value(dto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.added").value(dto.getAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        // Creates a matcher that matches if the examined String contains the specified String anywhere.
        //.andExpect(content().string(containsString("api/shoppinglists/1")));

    }

    @Test
    public void testThatShoppingListIsUpdatedReturns200() throws Exception {
        // Blir utan ID här men vi får id när vi .save() mha Service
        var dto = ShoppingListDto.builder()
                .id(1L)
                .added(LocalDate.now())
                .description("Test")
                .build();

        var savedDto = shoppingListService.save(dto);

        // Updated desc
        savedDto.setDescription("Updated desc");

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Dependency for serializing LocalDateTime
        final String listJSON = objectMapper.writeValueAsString(savedDto);

        mockMvc.perform(MockMvcRequestBuilders.put(apiRootURL + "/" + savedDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(listJSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.id").value(savedDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.description").value(savedDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.added").value(savedDto.getAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
    }

    @Test
    public void testThatListShoppingListsReturns200EmptyWhenNothing() throws Exception {
        shoppingListRepository.deleteAll(); // Så vi inte får conflict med data seed från data.sql, använder ej mocking här

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
                .id(3L)
                .added(LocalDate.now().plusDays(5))
                .description("random")
                .build();


        shoppingListService.save(targetLast);
        shoppingListService.save(random);
        shoppingListService.save(targetFirst);

        mockMvc.perform(MockMvcRequestBuilders.get(requestURL))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.[0].added").value(targetFirst.getAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

    }

    @Test
    public void testDeleteListThatDoesntExistReturns200WithDeleteResponseFalse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(apiRootURL + "/521521515"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.deleted").value(false));
    }

    @Test
    public void testDeleteListThatDoesExistReturns200WithDeleteResponseTrue() throws Exception {
        final ShoppingListDto shoppingListDto = testShoppingListDTO();
        var target = shoppingListService.save(shoppingListDto);

        mockMvc.perform(MockMvcRequestBuilders.delete(apiRootURL + "/" + target.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.deleted").value(true));
    }

}
