package com.alsomeb.shopletapi.service;

import com.alsomeb.shopletapi.entity.ShoppingList;

import java.util.List;

public interface ShoppingListService {
    List<ShoppingList> getAllShoppingLists();
    List<ShoppingList> getListsOrderByDateAsc();
    ShoppingList getById(long id);
    ShoppingList save(ShoppingList ShoppingList);
    boolean deleteById(long id);
    boolean doesListExist(ShoppingList shoppingList);
}
