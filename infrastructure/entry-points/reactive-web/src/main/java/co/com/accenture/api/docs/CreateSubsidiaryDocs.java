package co.com.accenture.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Component
@Schema(description = "Documentation for search a user by email and document id endpoint")
public class ExistsByEmailAndDocumentIdDocs {

    @Operation(
        operationId = "existsByEmailAndDocumentId",
        summary = "Get a user by email and document id",
        description = "Creates a new user in the system",
        tags = {"Users"}
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CreateUserResponseDTO.class))
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
            description = "User not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BussinessResponseException.class))
        )
    })
    public Mono<CreateUserResponseDTO> existsByEmailAndDocumentId(@RequestParam("email")  String email, @RequestParam("documentid") String documentId){
        return Mono.empty();
    }
}
