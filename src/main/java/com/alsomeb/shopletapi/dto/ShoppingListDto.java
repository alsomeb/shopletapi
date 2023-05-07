package com.alsomeb.shopletapi.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ShoppingListDto {
    private Long id;

    @NotBlank(message = "description is mandatory")
    @Size(min = 2, message = "Minimum 2 letters for desc")
    private String description;

    @NotNull(message = "missing date field 'added'")
    @FutureOrPresent(message = "date added must be present or in the future")
    private LocalDate added;
}
