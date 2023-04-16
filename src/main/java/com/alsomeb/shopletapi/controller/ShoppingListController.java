package com.alsomeb.shopletapi.controller;

import com.alsomeb.shopletapi.entity.ShoppingList;
import com.alsomeb.shopletapi.service.DeleteResponse;
import com.alsomeb.shopletapi.service.ShoppingListService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/shoppinglists")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingList>> getAllShoppingLists() {
        return new ResponseEntity<>(shoppingListService.getAllShoppingLists(), HttpStatus.OK);
    }

    @GetMapping(value = "sort", params = "order=asc")
    public ResponseEntity<List<ShoppingList>> getAllShoppingListsOrderDateAsc() {
        return new ResponseEntity<>(shoppingListService.getListsOrderByDateAsc(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public EntityModel<ShoppingList> getShoppingListById(@PathVariable long id) {
        var shoppingList = shoppingListService.getById(id);

        // Hateoas links
        return EntityModel.of(shoppingList,
                linkTo(methodOn(ShoppingListController.class).getShoppingListById(id)).withSelfRel(),
                linkTo(methodOn(ShoppingListController.class).getAllShoppingLists()).withRel("all-lists"));
    }

    // HATEOAS impl
    @PostMapping
    public ResponseEntity<EntityModel<ShoppingList>> addList(@Valid @RequestBody ShoppingList shoppingList) {
        // När vi post så får vi tbx en länk till resource som skapats.
        ShoppingList savedList = shoppingListService.save(shoppingList);

        EntityModel<ShoppingList> entityModel = EntityModel.of(shoppingList,
                linkTo(methodOn(ShoppingListController.class).getShoppingListById(savedList.getId())).withSelfRel(),
                linkTo(methodOn(ShoppingListController.class).getAllShoppingLists()).withRel("all-lists"));

        logger.info("POST Shoppinglist: {}", savedList);

        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<DeleteResponse> deleteListById(@PathVariable long id) {
        boolean deleted = shoppingListService.deleteById(id);
        return new ResponseEntity<>(new DeleteResponse(deleted), HttpStatus.OK);
    }


    // HATEOS Impl
    @PutMapping("{id}")
    public ResponseEntity<EntityModel<ShoppingList>> updateList(@Valid @RequestBody ShoppingList shoppingList, @PathVariable long id) {
        shoppingList.setId(id); // Id används för update, spelar ingen roll om resource inte finns pga databasen har senaste ID sequence när den skapar NY

        final boolean exist = shoppingListService.doesListExist(shoppingList);
        final ShoppingList savedList = shoppingListService.save(shoppingList);

        EntityModel<ShoppingList> entityModel = EntityModel.of(shoppingList,
                linkTo(methodOn(ShoppingListController.class).getShoppingListById(savedList.getId())).withSelfRel(),
                linkTo(methodOn(ShoppingListController.class).getAllShoppingLists()).withRel("all-lists"));

        if(exist) {
            logger.info("UPDATED Shoppinglist: {}", savedList);
            return new ResponseEntity<>(entityModel, HttpStatus.OK);
        }

        logger.info("POST Shoppinglist: {}", savedList);
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

}
