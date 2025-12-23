package co.com.accenture.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(name = "CreateProductRequestDTO", description = "Data required to create a new product")
public record CreateProductRequestDTO(

    @NotBlank(message = "Name cannot be blank")
    @Schema(description = "Name of the subsidiary", example = "Subsidiary A")
    String name,

    @Schema(description = "Stock of the product", example = "100")
    Integer stock

) {
}
