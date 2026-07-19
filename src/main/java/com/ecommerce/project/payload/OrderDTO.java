package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @Schema(description = "Unique identifier of the order", example = "1")
    private Long orderId;
    @Schema(description = "Email of the user who placed the order", example = "user@example.com")
    private String email;
    @Schema(description = "List of items in the order")
    private List<OrderItemDTO> orderItems;
    @Schema(description = "Date of the order", example = "2023-01-01")
    private LocalDate orderDate;
    @Schema(description = "Payment details for the order")
    private PaymentDTO payment;
    @Schema(description = "Total amount of the order", example = "100.0")
    private Double totalAmount;
    @Schema(description = "Status of the order", example = "PLACED")
    private String orderStatus;
    @Schema(description = "ID of the address for the order", example = "1")
    private Long addressId;
}
