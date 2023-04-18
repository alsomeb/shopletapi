package com.alsomeb.shopletapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "shopping_lists")
public class ShoppingListEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "Description is mandatory")
    private String description;

    @NotNull(message = "missing date field 'added'")
    @FutureOrPresent(message = "date added must be present or in the future")
    private LocalDate added;
}
