package com.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String productName, description, image;
    private Integer quantity;
    private Double price, specialPrice, discount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
