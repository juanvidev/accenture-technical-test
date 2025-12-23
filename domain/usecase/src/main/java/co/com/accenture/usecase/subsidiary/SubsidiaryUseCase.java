package co.com.accenture.usecase.subsidiary;

import co.com.accenture.model.exception.BusinessException;
import co.com.accenture.model.franchise.Franchise;
import co.com.accenture.model.franchise.gateways.FranchiseRepository;
import co.com.accenture.model.subsidary.Subsidiary;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SubsidiaryUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> saveSubsidiary(String idFromPath, Subsidiary subsidiary) {
        return franchiseRepository.findById(idFromPath)
                .switchIfEmpty(Mono.error(new BusinessException("BSS_002", "Franchise not found for the provided ID.")))
                .flatMap(franchise -> {
                    boolean exists = franchise.getSubsidiaries().stream()
                            .anyMatch(s -> s.getName().equalsIgnoreCase(subsidiary.getName()));

                    if (exists) {
                        return Mono.error(
                                new BusinessException("BSS_03", "Subsidiary already exists in franchise")
                        );
                    }

                    Subsidiary newSubsidiary = Subsidiary.builder()
                            .name(subsidiary.getName())
                            .products(subsidiary.getProducts())
                            .build();

                    franchise.getSubsidiaries().add(newSubsidiary);

                    return franchiseRepository.save(franchise);
                });

    }
}
