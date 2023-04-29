package com.alsomeb.shopletapi.controller;

import com.alsomeb.shopletapi.dto.ShoppingListDto;
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
@RequestMapping("api/v1/shoppinglists")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingListDto>> getAllShoppingLists() {
        return new ResponseEntity<>(shoppingListService.getAllShoppingLists(), HttpStatus.OK);
    }

    @GetMapping(value = "sort", params = "order=asc")
    public ResponseEntity<List<ShoppingListDto>> getAllShoppingListsOrderDateAsc() {
        return new ResponseEntity<>(shoppingListService.getListsOrderByDateAsc(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public EntityModel<ShoppingListDto> getShoppingListById(@PathVariable long id) {
        var shoppingList = shoppingListService.getById(id);

        // Hateoas links
        return EntityModel.of(shoppingList,
                linkTo(methodOn(ShoppingListController.class).getShoppingListById(id)).withSelfRel(),
                linkTo(methodOn(ShoppingListController.class).getAllShoppingLists()).withRel("all-lists"));
    }

    /*
    // HATEOAS impl, ----OBS PUT MAPPING KAN SKAPA OCH UPD, OBSOLET METOD ? --
    @PostMapping
    public ResponseEntity<EntityModel<ShoppingListDto>> addList(@Valid @RequestBody ShoppingListDto shoppingListDto) {
        // När vi post så får vi tbx en länk till resource som skapats.
        ShoppingListDto savedList = shoppingListService.save(shoppingListDto);

        EntityModel<ShoppingListDto> entityModel = EntityModel.of(shoppingListDto,
                linkTo(methodOn(ShoppingListController.class).getShoppingListById(savedList.getId())).withSelfRel(),
                linkTo(methodOn(ShoppingListController.class).getAllShoppingLists()).withRel("all-lists"));

        logger.info("POST Shoppinglist: {}", savedList);

        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }
     */

    @DeleteMapping("{id}")
    public ResponseEntity<DeleteResponse> deleteListById(@PathVariable long id) {
        boolean deleted = shoppingListService.deleteById(id);
        return new ResponseEntity<>(new DeleteResponse(deleted), HttpStatus.OK);
    }


    // HATEOS Impl
    @PutMapping("{id}")
    public ResponseEntity<EntityModel<ShoppingListDto>> updateList(@Valid @RequestBody ShoppingListDto shoppingListDto, @PathVariable long id) {
        shoppingListDto.setId(id); // Id används för update, spelar ingen roll om resource inte finns pga databasen har senaste ID sequence när den skapar NY

        final boolean exist = shoppingListService.doesListExist(shoppingListDto);
        final ShoppingListDto savedList = shoppingListService.save(shoppingListDto);

        EntityModel<ShoppingListDto> entityModel = EntityModel.of(savedList,
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
