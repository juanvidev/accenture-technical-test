package co.com.accenture.usecase.franchise;

import co.com.accenture.model.exception.BusinessException;
import co.com.accenture.model.franchise.Franchise;
import co.com.accenture.model.franchise.gateways.FranchiseRepository;
import co.com.accenture.model.product.Product;
import co.com.accenture.model.subsidary.Subsidiary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    private FranchiseUseCase franchiseUseCaseMock;
    private Franchise franchiseToTest;
    private Subsidiary subsidiaryToTest;
    private Product productToTest;

    @BeforeEach
    void setUp() {

        franchiseUseCaseMock = new FranchiseUseCase(franchiseRepository);

        productToTest = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Product A")
                .stock(100)
                .build();

        List<Subsidiary> subsidiaries = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        products.add(productToTest);

        subsidiaryToTest = Subsidiary.builder()
                .id(UUID.randomUUID().toString())
                .name("Subsidiary A")
                .products(products)
                .build();

        subsidiaries.add(subsidiaryToTest);

        franchiseToTest = Franchise.builder()
                .id(UUID.randomUUID().toString())
                .name("Franchise A")
                .subsidiaries(subsidiaries)
                .build();

    }


    @Test
    @DisplayName("Should save Franchise successfully!")
    void saveFranchiseTest() {
        when(franchiseRepository.existsByName(franchiseToTest.getName()))
                .thenReturn(Mono.just(false));
        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchiseToTest));

        StepVerifier.create(franchiseUseCaseMock.saveFranchise(franchiseToTest))
                .expectNext(franchiseToTest)
                .verifyComplete();

    }

    @Test
    @DisplayName("Should fail to save Franchise when name already exists")
    void failSaveFranchiseTest() {
        when(franchiseRepository.existsByName(franchiseToTest.getName()))
                .thenReturn(Mono.just(true));

        StepVerifier.create(franchiseUseCaseMock.saveFranchise(franchiseToTest))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().equals("A franchise with the same name already exists."))
                .verify();
    }


}

