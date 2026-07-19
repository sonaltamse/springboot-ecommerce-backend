package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    @Schema(description = "Unique identifier of the order item", example = "1")
    private Long orderItemId;
    @Schema(description = "Details of the product in the order")
    private ProductDTO product;
    @Schema(description = "Quantity of the product ordered", example = "2")
    private Integer quantity;
    @Schema(description = "Discount applied to the product", example = "0.1")
    private double discount;
    @Schema(description = "Price of the product at the time of ordering", example = "50.0")
    private double orderedProductPrice;
}
