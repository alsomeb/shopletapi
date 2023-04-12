package com.alsomeb.shopletapi.repository;

import com.alsomeb.shopletapi.entity.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

}
