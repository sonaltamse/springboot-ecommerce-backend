package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    @Schema(description = "Unique identifier of the address", example = "1")
    private Long addressId;
    @Schema(description = "Method of payment for the order", example = "CREDIT_CARD")
    private String paymentMethod;
    @Schema(description = "Name of the payment gateway", example = "STRIPE")
    private String pgName;
    @Schema(description = "Payment ID from the payment gateway", example = "pi_123456789")
    private String pgPaymentId;
    @Schema(description = "Status of the payment", example = "SUCCESS")
    private String pgStatus;
    @Schema(description = "Response message from the payment gateway", example = "Payment successful")
    private String pgResponseMessage;
}
