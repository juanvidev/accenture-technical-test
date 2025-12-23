package co.com.accenture.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(name = "CreateSubsidiaryRequestDTO", description = "Data required to create a new subsidiary")
public record CreateSubsidiaryRequestDTO(

    @NotBlank(message = "Name cannot be blank")
    @Schema(description = "Name of the subsidiary", example = "Subsidiary1")
    String name,

    @Schema(description = "List of products associated with the subsidiary", example = "[\"Product1\", \"Product2\"]")
    List<String> products

) {
}
