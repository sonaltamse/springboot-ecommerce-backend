package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    @Schema(description = "Unique identifier of the address", example = "1")
    private Long addressId;
    @Schema(description = "Street address", example = "123 Main St")
    private String street;
    @Schema(description = "Pincode", example = "123456")
    private String pincode;
    @Schema(description = "Building name", example = "Apt 4B")
    private String buildingName;
    @Schema(description = "City", example = "New York")
    private String city;
    @Schema(description = "Country", example = "USA")
    private String country;
    @Schema(description = "State", example = "NY")
    private String state;
}
