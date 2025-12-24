package co.com.accenture.model.franchise.gateways;

import co.com.accenture.model.franchise.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {
    Mono<Franchise> save(Franchise franchise);
    Mono<Boolean> existsByName(String name);
    Mono<Franchise> findById(String id);
    Flux<Franchise> findAll();
}
