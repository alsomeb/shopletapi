package com.alsomeb.shopletapi.dto;

import com.alsomeb.shopletapi.entity.ProductEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class ShoppingListDto {
    private Long id;

    @NotEmpty(message = "Description is mandatory")
    private String description;

    @NotNull(message = "missing date field 'added'")
    @FutureOrPresent(message = "date added must be present or in the future")
    private LocalDate added;

    @JsonIgnore
    private Set<ProductEntity> products;
}
