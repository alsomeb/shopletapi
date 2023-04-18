package com.alsomeb.shopletapi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ShoppingListDto {
    private Long id;
    private String description;
    private LocalDate added;
}
