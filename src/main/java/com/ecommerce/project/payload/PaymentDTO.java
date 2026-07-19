package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    @Schema(description = "Unique identifier of the payment", example = "1")
    private Long paymentId;
    @Schema(description = "Method of payment", example = "CREDIT_CARD")
    private String paymentMethod;
    @Schema(description = "Payment ID from the payment gateway", example = "pi_123456789")
    private String pgPaymentId;
    @Schema(description = "Status of the payment", example = "SUCCESS")
    private String pgStatus;
    @Schema(description = "Response message from the payment gateway", example = "Payment successful")
    private String pgResponseMessage;
    @Schema(description = "Name of the payment gateway", example = "STRIPE")
    private String pgName;
}
