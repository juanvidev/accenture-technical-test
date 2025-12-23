package co.com.accenture.model.subsidary.gateways;

import reactor.core.publisher.Mono;

public interface SubsidiaryRepository {
    Mono<Boolean> existsByName(String name);
}
