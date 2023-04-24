package com.alsomeb.shopletapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

// Guide: https://reflectoring.io/bean-validation-with-spring-boot/

@Data
@Builder
public class ProductDto {
    private Long id;

    @NotEmpty(message = "Product name is mandatory")
    @Size(min = 1, message = "Minimum 2 letters for product name")
    private String name;

    @Min(value = 1, message = "Min 1 product")
    private int amount;
}
