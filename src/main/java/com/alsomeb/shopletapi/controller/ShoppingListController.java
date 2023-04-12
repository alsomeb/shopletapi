package com.alsomeb.shopletapi.controller;

import com.alsomeb.shopletapi.entity.ShoppingList;
import com.alsomeb.shopletapi.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/shoppinglists")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingList>> getAllShoppingLists() {
        return new ResponseEntity<>(shoppingListService.getAllShoppingLists(), HttpStatus.OK);
    }

    @GetMapping(value = "sort", params = "order=desc")
    public ResponseEntity<List<ShoppingList>> getAllShoppingListsOrderDateDesc() {
        return new ResponseEntity<>(shoppingListService.getListsOrderByDateDesc(), HttpStatus.OK);
    }
}
