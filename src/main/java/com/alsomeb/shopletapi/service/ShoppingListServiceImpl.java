package com.alsomeb.shopletapi.service;

import com.alsomeb.shopletapi.entity.ShoppingList;
import com.alsomeb.shopletapi.exception.ShoppingListNotFoundException;
import com.alsomeb.shopletapi.repository.ShoppingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingListServiceImpl implements ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;

    @Autowired
    public ShoppingListServiceImpl(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    // CRUD + getListsOrderByDateDesc()
    @Override
    public List<ShoppingList> getAllShoppingLists() {
        return shoppingListRepository.findAll();
    }

    @Override
    public List<ShoppingList> getListsOrderByDateAsc() {
        return shoppingListRepository.findAll().stream()
                .sorted(Comparator.comparing(ShoppingList::getAdded))
                .collect(Collectors.toList());
    }

    @Override
    public ShoppingList getById(long id) {
        return shoppingListRepository.findById(id)
                .orElseThrow(() -> new ShoppingListNotFoundException("Cant find ShoppingList with id: " + id));
    }

    @Override
    public ShoppingList save(ShoppingList shoppingList) {
        return shoppingListRepository.save(shoppingList);
    }
}
