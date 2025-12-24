package co.com.accenture.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "UpdateProductStockRequestDTO", description = "Data required to update the stock status of a product")
public record UpdateProductStockRequestDTO(

     @NotBlank(message = "Product name must not be blank")
     @Schema(description = "Product name", example = "Laptop")
     String productName,

     @NotNull( message = "Stock quantity must not be null")
     @Schema(description = "New stock quantity of the product", example = "50")
     Integer stock
) {}
