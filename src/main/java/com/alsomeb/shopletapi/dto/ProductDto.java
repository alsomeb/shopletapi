package com.alsomeb.shopletapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private Long id;

    @NotEmpty(message = "Product name is mandatory")
    private String name;

    @NotEmpty(message = "amount is mandatory")
    @Size(min = 1, message = "Minimum 1 product")
    private int amount;
}
