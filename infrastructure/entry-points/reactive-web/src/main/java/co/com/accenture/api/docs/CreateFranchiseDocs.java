package co.com.accenture.api.docs;

import co.com.accenture.api.dto.request.CreateFranchiseRequestDTO;
import co.com.accenture.api.dto.response.CreateFranchiseResponseDTO;
import co.com.accenture.api.exception.BussinessResponseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Schema(description = "Documentation for create Franchises endpoint")
public class CreateFranchiseDocs {

    @Operation(
        operationId = "createFranchise",
        summary = "Create a new franchise",
        description = "Creates a new franchise in the system",
        tags = {"Franchises"}
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Franchise created successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CreateFranchiseRequestDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BussinessResponseException.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BussinessResponseException.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Franchise already exists",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BussinessResponseException.class))
        )
    })
    public Mono<CreateFranchiseResponseDTO> createFranchise(
        @RequestBody(description = "Franchise payload", required = true) CreateFranchiseResponseDTO dto) {
        return Mono.empty();
    }
}
