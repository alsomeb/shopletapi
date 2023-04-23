package com.alsomeb.shopletapi.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ShoppingListDto {
    private Long id;

    @NotEmpty(message = "Description is mandatory")
    private String description;

    @NotNull(message = "missing date field 'added'")
    @FutureOrPresent(message = "date added must be present or in the future")
    private LocalDate added;
}
