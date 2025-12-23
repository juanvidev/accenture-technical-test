package co.com.accenture.api;

import co.com.accenture.api.dto.request.CreateFranchiseRequestDTO;
import co.com.accenture.api.mapper.FranchiseMapper;
import co.com.accenture.usecase.franchise.FranchiseUseCase;
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
    private final FranchiseMapper franchiseMapper;

    public Mono<ServerResponse> listenSaveFranchise(ServerRequest serverRequest) {
        System.out.println("[LISTEN SAVE FRANCHISE]");
        return serverRequest.bodyToMono(CreateFranchiseRequestDTO.class)
                .doOnNext(dto -> System.out.println("[Handler] listenSaveUser - Received payload"))
                .map(franchiseMapper::toDomain)
                .flatMap(franchiseUseCase::saveFranchise)
                .doOnNext(entity -> System.out.println("[Handler] listenSaveUser  - User saved: " + entity))
                .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).build())
                .doOnError(error -> System.err.println("[Handler] listenSaveUser - Error: " + error.getMessage()))
                .doOnSuccess(entity -> System.out.println("[Handler] listenSaveUser - User saved: " + entity));
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }
}
