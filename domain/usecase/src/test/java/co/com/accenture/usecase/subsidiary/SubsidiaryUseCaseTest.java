package co.com.accenture.usecase.subsidiary;

import co.com.accenture.model.exception.BusinessException;
import co.com.accenture.model.franchise.Franchise;
import co.com.accenture.model.franchise.gateways.FranchiseRepository;
import co.com.accenture.model.product.Product;
import co.com.accenture.model.subsidary.Subsidiary;
import co.com.accenture.usecase.product.ProductUseCase;
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
class SubsidiaryUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    private SubsidiaryUseCase subsidiaryUseCaseMock;
    private Franchise franchiseToTest;
    private Subsidiary subsidiaryToTest;
    private Product productToTest;

    @BeforeEach
    void setUp() {

        subsidiaryUseCaseMock = new SubsidiaryUseCase(franchiseRepository);

        List<Subsidiary> subsidiaries = new ArrayList<>();

        subsidiaryToTest = Subsidiary.builder()
                .id(UUID.randomUUID().toString())
                .name("Subsidiary A")
                .products(new ArrayList<>(List.of()))
                .build();

        subsidiaries.add(subsidiaryToTest);

        franchiseToTest = Franchise.builder()
                .id(UUID.randomUUID().toString())
                .name("Franchise A")
                .subsidiaries(new ArrayList<>(List.of()))
                .build();

    }


    @Test
    @DisplayName("Should save Subsidiary successfully!")
    void saveProductTest() {
        when(franchiseRepository.findById(franchiseToTest.getId()))
                .thenReturn(Mono.just(franchiseToTest));

        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchiseToTest));

        StepVerifier.create(subsidiaryUseCaseMock.saveSubsidiary(franchiseToTest.getId(), subsidiaryToTest))
                .expectNext(franchiseToTest)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should fail to save Subisidiary when franchise not found")
    void failSaveProductFranchiseNotFoundTest() {
        when(franchiseRepository.findById(franchiseToTest.getId()))
                .thenReturn(Mono.empty());

        StepVerifier.create(subsidiaryUseCaseMock.saveSubsidiary(franchiseToTest.getId(), subsidiaryToTest))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().equals("Franchise not found for the provided ID."))
                .verify();
    }

    @Test
    @DisplayName("Should fail to save Subsidiary when name already exists")
    void failSaveProductTest() {

        franchiseToTest.getSubsidiaries().add(subsidiaryToTest);

        when(franchiseRepository.findById(franchiseToTest.getId()))
                .thenReturn(Mono.just(franchiseToTest));

        StepVerifier.create(subsidiaryUseCaseMock.saveSubsidiary(franchiseToTest.getId(), subsidiaryToTest))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().equals("Subsidiary already exists in franchise"))
                .verify();

    }


}

