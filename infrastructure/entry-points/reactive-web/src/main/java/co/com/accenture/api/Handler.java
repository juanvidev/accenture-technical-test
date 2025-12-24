package co.com.accenture.api;

import co.com.accenture.api.dto.request.CreateFranchiseRequestDTO;
import co.com.accenture.api.dto.request.CreateProductRequestDTO;
import co.com.accenture.api.dto.request.CreateSubsidiaryRequestDTO;
import co.com.accenture.api.dto.request.UpdateProductStockRequestDTO;
import co.com.accenture.api.dto.response.GenericResponse;
import co.com.accenture.api.dto.response.ListFranchisesResponseDTO;
import co.com.accenture.api.mapper.FranchiseMapper;
import co.com.accenture.api.mapper.ProductMapper;
import co.com.accenture.api.mapper.SubsidiaryMapper;
import co.com.accenture.api.util.ValidatorUtil;
import co.com.accenture.usecase.franchise.FranchiseUseCase;
import co.com.accenture.usecase.product.ProductUseCase;
import co.com.accenture.usecase.subsidiary.SubsidiaryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class Handler {
    private final FranchiseUseCase franchiseUseCase;
    private final SubsidiaryUseCase subsidiaryUseCase;
    private final ProductUseCase productUseCase;
    private final FranchiseMapper franchiseMapper;
    private final SubsidiaryMapper subsidiaryMapper;
    private final ProductMapper productMapper;
    private final ValidatorUtil validatorUtil;

    public Mono<ServerResponse> listenSaveFranchise(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateFranchiseRequestDTO.class)
                .map(franchiseMapper::toDomain)
                .flatMap(validatorUtil::validate)
                .flatMap(franchiseUseCase::saveFranchise)
                .doOnNext(entity -> System.out.println("[Handler] listenSaveFranchise  - Franchise saved: " + entity.getName()))
                .flatMap(franchise -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(
                                GenericResponse
                                    .builder()
                                    .success(true)
                                    .data(franchise)
                                    .timestamp(LocalDateTime.now())
                                    .message("Franchise created")
                                    .build()
                ))
                .doOnError(error -> System.err.println("[Handler] listenSaveFranchise - Error: " + error.getMessage()));
    }

    public Mono<ServerResponse> listenSaveSubsidiary(ServerRequest serverRequest) {

        String idFromPath = serverRequest.pathVariable("id");

        return serverRequest.bodyToMono(CreateSubsidiaryRequestDTO.class)
                .doOnNext(dto -> System.out.println("[Handler] listenSaveSubsidiary - Received payload"))
                .map(subsidiaryMapper::toDomain)
                .flatMap(validatorUtil::validate)
                .flatMap(subsidiary -> subsidiaryUseCase.saveSubsidiary(idFromPath, subsidiary))
                .doOnNext(entity -> System.out.println("[Handler] listenSaveSubsidiary  - Subsidiary saved: " + entity))
                .flatMap(sub ->
                        ServerResponse.status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(
                                        GenericResponse
                                                .builder()
                                                .success(true)
                                                .data(sub)
                                                .timestamp(LocalDateTime.now())
                                                .message("Subsidiary created")
                                                .build()
                )
                .doOnError(error -> System.err.println("[Handler] listenSaveSubsidiary - Error: " + error.getMessage()))
                .doOnSuccess(entity -> System.out.println("[Handler] listenSaveSubsidiary - Subsidiary saved: " + entity)));
    }

    public Mono<ServerResponse> listenSaveProduct(ServerRequest serverRequest) {

        String idFranchisePath = serverRequest.pathVariable("franchiseId");
        String idSubsidiaryPath = serverRequest.pathVariable("subsidiaryId");

        return serverRequest.bodyToMono(CreateProductRequestDTO.class)
                .doOnNext(dto -> System.out.println("[Handler] listenSaveProduct - Received payload"))
                .map(productMapper::toDomain)
                .flatMap(validatorUtil::validate)
                .flatMap(product -> productUseCase.saveProduct(idFranchisePath, idSubsidiaryPath, product))
                .doOnNext(entity -> System.out.println("[Handler] listenSaveProduct  - Product saved: " + entity))
                .flatMap(product ->
                        ServerResponse.status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(
                                        GenericResponse
                                                .builder()
                                                .success(true)
                                                .data(product)
                                                .timestamp(LocalDateTime.now())
                                                .message("Product created")
                                                .build()
                )
                .doOnError(error -> System.err.println("[Handler] listenSaveProduct - Error: " + error.getMessage()))
                .doOnSuccess(entity -> System.out.println("[Handler] listenSaveProduct - Product saved: " + entity)));
    }

    public Mono<ServerResponse> listenUpdateProductStock(ServerRequest serverRequest) {

        String idFranchisePath = serverRequest.pathVariable("franchiseId");
        String idSubsidiaryPath = serverRequest.pathVariable("subsidiaryId");

        return serverRequest.bodyToMono(UpdateProductStockRequestDTO.class)
                .doOnNext(dto -> System.out.println("[Handler] listenUpdateProductStock - Received payload"))
                .flatMap(validatorUtil::validate)
                .flatMap(dto -> productUseCase.updateProductStock(idFranchisePath, idSubsidiaryPath, dto.productName(), dto.stock()))
                .doOnNext(entity -> System.out.println("[Handler] listenUpdateProductStock  - Product stock updated: " + entity))
                .flatMap(franchise -> ServerResponse.status(HttpStatus.OK).build())
                .doOnError(error -> System.err.println("[Handler] listenUpdateProductStock - Error: " + error.getMessage()))
                .doOnSuccess(entity -> System.out.println("[Handler] listenUpdateProductStock - Product stock updated: " + entity));
    }

    public Mono<ServerResponse> listenGetProductWithMostStock(ServerRequest serverRequest) {

        String idFranchisePath = serverRequest.pathVariable("id");

        return productUseCase.getProductWithMostStock(idFranchisePath)
                .map(productMapper::toResponseMaxStock)
                .collectList()
                .flatMap(products -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(GenericResponse
                                .builder()
                                .success(true)
                                .data(products)
                                .timestamp(LocalDateTime.now())
                                .message("Products with most stock found")
                                .build()
                        )
                )
                .doOnError(error -> System.err.println("[Handler] listenGetProductWithMostStock - Error: " + error.getMessage()))
                .doOnSuccess(entity -> System.out.println("[Handler] listenGetProductWithMostStock - Products with most stock found: " + entity));

    }

    public Mono<ServerResponse> listenUpdateFranchise(ServerRequest serverRequest) {
        String idFromPath = serverRequest.pathVariable("id");

        return serverRequest.bodyToMono(CreateFranchiseRequestDTO.class)
                .doOnNext(dto -> System.out.println("[Handler] listenUpdateFranchise - Received payload"))
                .map(franchiseMapper::toDomain)
                .flatMap(validatorUtil::validate)
                .flatMap(franchise -> franchiseUseCase.updateFranchise(idFromPath, franchise))
                .doOnNext(entity -> System.out.println("[Handler] listenUpdateFranchise  - Franchise updated: " + entity))
                .flatMap(fra ->
                        ServerResponse.status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(
                                        GenericResponse
                                                .builder()
                                                .success(true)
                                                .data(fra)
                                                .timestamp(LocalDateTime.now())
                                                .message("Franchise updated")
                                                .build()
                )
                .doOnError(error -> System.err.println("[Handler] listenUpdateFranchise - Error: " + error.getMessage()))
                .doOnSuccess(entity -> System.out.println("[Handler] listenUpdateFranchise - Franchise updated: " + entity)));
    }

    public Mono<ServerResponse> listenUpdateSubsidiary(ServerRequest serverRequest) {
        String idFranchisePath = serverRequest.pathVariable("franchiseId");
        String idSubsidiaryPath = serverRequest.pathVariable("subsidiaryId");

        return serverRequest.bodyToMono(CreateSubsidiaryRequestDTO.class)
                .doOnNext(dto -> System.out.println("[Handler] listenUpdateSubsidiary - Received payload"))
                .map(subsidiaryMapper::toDomain)
                .flatMap(validatorUtil::validate)
                .flatMap(subsidiary -> subsidiaryUseCase.updateSubsidiary(idFranchisePath, idSubsidiaryPath, subsidiary))
                .doOnNext(entity -> System.out.println("[Handler] listenUpdateSubsidiary  - Subsidiary updated: " + entity))
                .flatMap(sub ->
                        ServerResponse.status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(
                                        GenericResponse
                                                .builder()
                                                .success(true)
                                                .data(sub)
                                                .timestamp(LocalDateTime.now())
                                                .message("Subsidiary updated")
                                                .build()
                )
                .doOnError(error -> System.err.println("[Handler] listenUpdateSubsidiary - Error: " + error.getMessage()))
                .doOnSuccess(entity -> System.out.println("[Handler] listenUpdateSubsidiary - Subsidiary updated: " + entity)));
    }

    public Mono<ServerResponse> listenGetAllFranchises(ServerRequest serverRequest) {

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(franchiseUseCase.getAllFranchises(), ListFranchisesResponseDTO.class);
    }

}
