package co.com.accenture.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MaxStockProductResponseDTO", description = "Data of the product with the highest stock in a subsidiary")
public record MaxStockProductsResponseDTO(
        String subsidiaryName,
        String productName,
        Integer stock
) {}

