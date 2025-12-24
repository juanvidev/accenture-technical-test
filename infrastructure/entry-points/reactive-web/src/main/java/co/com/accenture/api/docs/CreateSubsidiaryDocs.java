package co.com.accenture.api.docs;

import co.com.accenture.api.dto.request.CreateSubsidiaryRequestDTO;
import co.com.accenture.api.dto.response.CreateFranchiseResponseDTO;
import co.com.accenture.api.dto.response.CreateSubsidiaryResponseDTO;
import co.com.accenture.api.exception.BussinessResponseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Component
@Schema(name = "CreateSubsidiaryDocs", description = "Documentation for creating a subsidiary")
public class CreateSubsidiaryDocs {

    @Operation(
        operationId = "createSubsidiary",
        summary = "Create a new subsidiary",
        description = "Creates a new subsidiary provided id of a franchise",
        tags = {"Subsidiaries"}
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Subsidiary created successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CreateSubsidiaryRequestDTO.class))
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
            description = "Subsidiary already exists",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BussinessResponseException.class))
        )
    })
    public Mono<CreateSubsidiaryResponseDTO> createSubsidiary(@RequestParam("id")  String idFranchise){
        return Mono.empty();
    }
}
