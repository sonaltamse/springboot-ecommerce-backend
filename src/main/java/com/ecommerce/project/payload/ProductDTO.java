package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @Schema(description = "Unique identifier of the product", example = "1")
    private Long productId;
    @Schema(description = "Name of the product", example = "Laptop")
    private String productName;
    @Schema(description = "Description of the product", example = "High-performance laptop for gaming")
    private String description;
    @Schema(description = "Image URL of the product", example = "https://example.com/laptop.jpg")
    private String image;
    @Schema(description = "Available quantity of the product", example = "10")
    private Integer quantity;
    @Schema(description = "Price of the product", example = "1000.0")
    private Double price;
    @Schema(description = "Discount of the product", example = "100.0")
    private Double discount;
    @Schema(description = "Special price of the product", example = "900.0")
    private Double specialPrice;
}
