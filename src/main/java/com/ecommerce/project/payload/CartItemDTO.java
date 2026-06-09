package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private Integer quantity;
    private CartDTO cart;
    private ProductDTO productDTO;
    private Double discount;
    private Double productPrice;
}
