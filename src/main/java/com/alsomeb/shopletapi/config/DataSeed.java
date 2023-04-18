/*package com.alsomeb.shopletapi.config;

import com.alsomeb.shopletapi.entity.ShoppingListEntity;
import com.alsomeb.shopletapi.repository.ShoppingListRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@Configuration
public class DataSeed {

    final ShoppingListRepository shoppingListRepository;

    public DataSeed(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    @Bean
    public CommandLineRunner seedData(ShoppingListRepository shoppingListRepository) {
        return (args -> {
            List<ShoppingListEntity> data = Arrays.asList(
                    ShoppingListEntity.builder()
                            .description("Groceries for dinner with dad")
                            .added(LocalDate.now().plusDays(10))
                            .build(),

                    ShoppingListEntity.builder()
                            .description("Buy Snacks For The Weekend")
                            .added(LocalDate.now().plusDays(5))
                            .build(),

                    ShoppingListEntity.builder()
                            .description("Birthday Shopping for cake")
                            .added(LocalDate.now())
                            .build()
            );

            shoppingListRepository.saveAll(data);
        });
    }
}

*/