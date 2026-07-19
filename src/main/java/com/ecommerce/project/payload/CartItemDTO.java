package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    @Schema(description = "Unique identifier for the cart item", example = "1")
    private Long cartItemId;
    @Schema(description = "Quantity of the product in the cart", example = "2")
    private Integer quantity;
    @Schema(description = "Cart to which the item belongs")
    private CartDTO cart;
    @Schema(description = "Product details")
    private ProductDTO productDTO;
    @Schema(description = "Discount applied to the cart item", example = "10.0")
    private Double productPrice;
}
