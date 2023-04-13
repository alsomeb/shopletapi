package com.alsomeb.shopletapi.service;

import com.alsomeb.shopletapi.entity.ShoppingList;

import java.util.List;

public interface ShoppingListService {
    List<ShoppingList> getAllShoppingLists();
    List<ShoppingList> getListsOrderByDateDesc();
    ShoppingList getById(long id);
}
