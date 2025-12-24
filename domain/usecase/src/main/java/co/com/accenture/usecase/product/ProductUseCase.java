package co.com.accenture.usecase.product;

import co.com.accenture.model.exception.BusinessException;
import co.com.accenture.model.franchise.Franchise;
import co.com.accenture.model.franchise.gateways.FranchiseRepository;
import co.com.accenture.model.product.Product;
import co.com.accenture.model.product.ProductWithSubsidiary;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@RequiredArgsConstructor
public class ProductUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> saveProduct(String idFranchise, String idSubsidiary, Product product) {
        return franchiseRepository.findById(idFranchise)
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

    public Mono<Franchise> updateProductStock(String idFranchise, String idSubsidiary, String productName, Integer stockToAdd) {
        return franchiseRepository.findById(idFranchise)
                .switchIfEmpty(Mono.error(new BusinessException("BSS_002", "Franchise not found for the provided ID.")))
                .flatMap(franchise -> {
                    var subsidiaryOpt = franchise.getSubsidiaries().stream()
                            .filter(subsidiary -> subsidiary.getId().equalsIgnoreCase(idSubsidiary))
                            .findFirst();

                    if (subsidiaryOpt.isEmpty()) {
                        return Mono.error(new BusinessException("BSS_004", "Subsidiary not found for the provided ID."));
                    }

                    var subsidiary = subsidiaryOpt.get();

                    var productOpt = subsidiary.getProducts().stream()
                            .filter(p -> p.getName().equalsIgnoreCase(productName))
                            .findFirst();

                    if (productOpt.isEmpty()) {
                        return Mono.error(new BusinessException("BSS_006", "Product not found in subsidiary."));
                    }

                    var product = productOpt.get();
                    product.setStock(stockToAdd);

                    return franchiseRepository.save(franchise);
                });
    }

    public Flux<ProductWithSubsidiary> getProductWithMostStock(String idFranchise) {

        return franchiseRepository.findById(idFranchise)
                .switchIfEmpty(Mono.error(new BusinessException("BSS_002", "Franchise not found for the provided ID.")))
                .flatMapMany(franchise ->
                     Flux.fromIterable(franchise.getSubsidiaries())
                        .flatMap(subsidiary ->
                            Flux.fromIterable(subsidiary.getProducts())
                                .collectList()
                                .flatMapMany(products ->
                                        products.stream()
                                                .max(Comparator.comparing(Product::getStock))
                                                .map(Flux::just)
                                                .orElseGet(Flux::empty)
                                )
                                .map(product -> ProductWithSubsidiary
                                        .builder()
                                        .productId(product.getId())
                                        .productName(product.getName())
                                        .stock(product.getStock())
                                        .subsidiaryName(subsidiary.getName())
                                        .build()
                                )
                        )
                );
    }

}
