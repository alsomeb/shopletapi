package com.alsomeb.shopletapi.service;

import com.alsomeb.shopletapi.dto.ProductDto;


import java.util.Set;

public interface ProductService {
    Set<ProductDto> findAllProductsByShoppingListId(long id);

    ProductDto saveProductToShoppingList(Long listId, ProductDto productDto);
}
