package com.alsomeb.shopletapi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private Long id;

    @NotEmpty(message = "Product name is mandatory")
    private String name;
}
