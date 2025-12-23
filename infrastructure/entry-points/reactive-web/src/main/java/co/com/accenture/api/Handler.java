package co.com.accenture.api;

import co.com.accenture.api.dto.request.CreateFranchiseRequestDTO;
import co.com.accenture.api.dto.request.CreateSubsidiaryRequestDTO;
import co.com.accenture.api.mapper.FranchiseMapper;
import co.com.accenture.api.mapper.SubsidiaryMapper;
import co.com.accenture.api.util.ValidatorUtil;
import co.com.accenture.usecase.franchise.FranchiseUseCase;
import co.com.accenture.usecase.subsidiary.SubsidiaryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final FranchiseUseCase franchiseUseCase;
    private final SubsidiaryUseCase subsidiaryUseCase;
    private final FranchiseMapper franchiseMapper;
    private final SubsidiaryMapper subsidiaryMapper;
    private final ValidatorUtil validatorUtil;

    public Mono<ServerResponse> listenSaveFranchise(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateFranchiseRequestDTO.class)
                .doOnNext(dto -> System.out.println("[Handler] listenSaveUser - Received payload"))
                .map(franchiseMapper::toDomain)
                .flatMap(validatorUtil::validate)
                .flatMap(franchiseUseCase::saveFranchise)
                .doOnNext(entity -> System.out.println("[Handler] listenSaveUser  - User saved: " + entity))
                .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).build())
                .doOnError(error -> System.err.println("[Handler] listenSaveUser - Error: " + error.getMessage()))
                .doOnSuccess(entity -> System.out.println("[Handler] listenSaveUser - User saved: " + entity));
    }

    public Mono<ServerResponse> listenSaveSubsidiary(ServerRequest serverRequest) {

        String idFromPath = serverRequest.pathVariable("id");

        return serverRequest.bodyToMono(CreateSubsidiaryRequestDTO.class)
                .doOnNext(dto -> System.out.println("[Handler] listenSaveSubsidiary - Received payload"))
                .map(subsidiaryMapper::toDomain)
                .flatMap(subsidiary -> subsidiaryUseCase.saveSubsidiary(idFromPath, subsidiary))
                .doOnNext(entity -> System.out.println("[Handler] listenSaveSubsidiary  - Subsidiary saved: " + entity))
                .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).build())
                .doOnError(error -> System.err.println("[Handler] listenSaveSubsidiary - Error: " + error.getMessage()))
                .doOnSuccess(entity -> System.out.println("[Handler] listenSaveSubsidiary - Subsidiary saved: " + entity));
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }
}
