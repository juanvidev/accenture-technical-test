package co.com.accenture.usecase.product;

import co.com.accenture.model.exception.BusinessException;
import co.com.accenture.model.franchise.Franchise;
import co.com.accenture.model.franchise.gateways.FranchiseRepository;
import co.com.accenture.model.product.Product;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> saveProduct(String idFranchise, String idSubsidiary, Product product) {
        return franchiseRepository.findById(idFranchise)
                .flatMap(franchise -> franchiseRepository.findById(idFranchise))
                .switchIfEmpty(Mono.error(new BusinessException("BSS_002", "Franchise not found for the provided ID.")))
                .flatMap(franchise -> {
                    var subsidiaryOpt = franchise.getSubsidiaries().stream()
                            .filter(subsidiary -> subsidiary.getId().equalsIgnoreCase(idSubsidiary))
                            .findFirst();

                    if (subsidiaryOpt.isEmpty()) {
                        return Mono.error(new BusinessException("BSS_004", "Subsidiary not found for the provided ID."));
                    }

                    var subsidiary = subsidiaryOpt.get();

                    boolean productExists = subsidiary.getProducts().stream()
                            .anyMatch(p -> p.getName().equalsIgnoreCase(product.getName()));

                    if (productExists) {
                        return Mono.error(new BusinessException("BSS_005", "Product already exists in subsidiary."));
                    }

                    Product productNew = Product.builder()
                            .name(product.getName())
                            .stock(product.getStock())
                            .build();

                    subsidiary.getProducts().add(productNew);
                    return franchiseRepository.save(franchise);
                });


    }
}
