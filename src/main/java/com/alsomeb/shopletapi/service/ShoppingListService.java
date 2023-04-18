package com.alsomeb.shopletapi.service;

import com.alsomeb.shopletapi.dto.ShoppingListDto;

import java.util.List;

public interface ShoppingListService {
    List<ShoppingListDto> getAllShoppingLists();
    List<ShoppingListDto> getListsOrderByDateAsc();
    ShoppingListDto getById(long id);
    ShoppingListDto save(ShoppingListDto shoppingListDto);
    boolean deleteById(long id);
    boolean doesListExist(ShoppingListDto shoppingListDto);
}
