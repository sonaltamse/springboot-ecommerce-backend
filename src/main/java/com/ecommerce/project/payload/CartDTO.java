package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    @Schema(description = "Unique identifier of the cart", example = "1")
    private Long cartId;
    @Schema(description = "Total price of the cart", example = "100.0")
    private Double totalPrice=0.0;
    @Schema(description = "List of products in the cart")
    private List<ProductDTO> products = new ArrayList<>();
}
