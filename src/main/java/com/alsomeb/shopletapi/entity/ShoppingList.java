package com.alsomeb.shopletapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ShoppingList {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "Description is mandatory")
    private String description;

    @FutureOrPresent(message = "date added must be present or in the future")
    private LocalDate added;
}
