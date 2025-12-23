package co.com.accenture.usecase.franchise;

import co.com.accenture.model.exception.BusinessException;
import co.com.accenture.model.franchise.Franchise;
import co.com.accenture.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {
    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> saveFranchise(Franchise franchise) {
        return franchiseRepository.existsByName(franchise.getName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BusinessException("BSS_001","A franchise with the same name already exists."));
                    } else {
                        return franchiseRepository.save(franchise);
                    }
                });


    }

    public Mono<Boolean> existsByName(String name) {
        return franchiseRepository.existsByName(name)
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found with the provided name.")))
                .thenReturn(true);

    }
}
