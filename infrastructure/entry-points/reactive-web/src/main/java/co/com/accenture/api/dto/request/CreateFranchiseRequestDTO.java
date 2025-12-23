package co.com.accenture.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;

@Schema(description = "Request DTO for creating a new franchise applicant")
public record CreateFranchiseRequestDTO(

    @NotBlank(message = "Name cannot be blank")
    @Schema(description = "Franchise's name", example = "Macdonald")
    String name,

    @Schema(description = "List of subsidiaries", example = "[\"Subsidiary1\", \"Subsidiary2\"]")
    List<String> subsidiaries

) {
}
