package co.com.accenture.usecase.product;

import co.com.accenture.model.exception.BusinessException;
import co.com.accenture.model.franchise.Franchise;
import co.com.accenture.model.franchise.gateways.FranchiseRepository;
import co.com.accenture.model.product.Product;
import co.com.accenture.model.product.ProductWithSubsidiary;
import co.com.accenture.model.subsidary.Subsidiary;
import co.com.accenture.usecase.franchise.FranchiseUseCase;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    private ProductUseCase productUseCaseMock;
    private Franchise franchiseToTest;
    private Subsidiary subsidiaryToTest;
    private Product productToTest;
    private ProductWithSubsidiary productWithSubsidiaryToTest;

    @BeforeEach
    void setUp() {

        productUseCaseMock = new ProductUseCase(franchiseRepository);

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
                .products(new ArrayList<>(List.of()))
                .build();

        subsidiaries.add(subsidiaryToTest);

        franchiseToTest = Franchise.builder()
                .id(UUID.randomUUID().toString())
                .name("Franchise A")
                .subsidiaries(subsidiaries)
                .build();

        productWithSubsidiaryToTest = ProductWithSubsidiary.builder()
                .productId(productToTest.getId())
                .productName(productToTest.getName())
                .stock(productToTest.getStock())
                .subsidiaryName(subsidiaryToTest.getName())
                .build();

    }


    @Test
    @DisplayName("Should save Product successfully!")
    void saveProductTest() {
        when(franchiseRepository.findById(franchiseToTest.getId()))
                .thenReturn(Mono.just(franchiseToTest));

        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchiseToTest));

        StepVerifier.create(productUseCaseMock.saveProduct(franchiseToTest.getId(), subsidiaryToTest.getId(), productToTest))
                .expectNext(franchiseToTest)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should fail to save Product when franchise not found")
    void failSaveProductFranchiseNotFoundTest() {
        when(franchiseRepository.findById(franchiseToTest.getId()))
                .thenReturn(Mono.empty());

        StepVerifier.create(productUseCaseMock.saveProduct(franchiseToTest.getId(), subsidiaryToTest.getId(), productToTest))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().equals("Franchise not found for the provided ID."))
                .verify();
    }

    @Test
    @DisplayName("Should fail to save Product when subsidiary not found")
    void failSaveProductSubsidiaryNotFoundTest() {
        when(franchiseRepository.findById(franchiseToTest.getId()))
                .thenReturn(Mono.just(franchiseToTest));

        StepVerifier.create(productUseCaseMock.saveProduct(franchiseToTest.getId(), "invalid-subsidiary-id", productToTest))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().equals("Subsidiary not found for the provided ID."))
                .verify();
    }


    @Test
    @DisplayName("Should fail to save Product when name already exists")
    void failSaveProductTest() {

        subsidiaryToTest.getProducts().add(productToTest);
        franchiseToTest.getSubsidiaries().clear();
        franchiseToTest.getSubsidiaries().add(subsidiaryToTest);

        when(franchiseRepository.findById(franchiseToTest.getId()))
                .thenReturn(Mono.just(franchiseToTest));

        StepVerifier.create(productUseCaseMock.saveProduct(franchiseToTest.getId(), subsidiaryToTest.getId(), productToTest))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().equals("Product already exists in subsidiary."))
                .verify();

    }

    @Test
    @DisplayName("Should update Product stock successfully!")
    void updateProductStockTest() {

        subsidiaryToTest.getProducts().add(productToTest);

        when(franchiseRepository.findById(franchiseToTest.getId()))
                .thenReturn(Mono.just(franchiseToTest));

        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchiseToTest));

        StepVerifier.create(productUseCaseMock.updateProductStock(franchiseToTest.getId(), subsidiaryToTest.getId(), productToTest.getName(), 50))
                .expectNext(franchiseToTest)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should fail to update Product stock when product not found")
    void failUpdateProductStockNotFoundTest() {
        when(franchiseRepository.findById(franchiseToTest.getId()))
                .thenReturn(Mono.just(franchiseToTest));

        StepVerifier.create(productUseCaseMock.updateProductStock(franchiseToTest.getId(), subsidiaryToTest.getId(), "Invalid Product", 50))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().equals("Product not found in subsidiary."))
                .verify();
    }

    @Test
    @DisplayName("Should fail to update Product stock when subsidiary not found")
    void failUpdateProductStockSubsidiaryNotFoundTest() {
        when(franchiseRepository.findById(franchiseToTest.getId()))
                .thenReturn(Mono.just(franchiseToTest));

        StepVerifier.create(productUseCaseMock.updateProductStock(franchiseToTest.getId(), "invalid-subsidiary-id", productToTest.getName(), 50))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().equals("Subsidiary not found for the provided ID."))
                .verify();
    }

    @Test
    @DisplayName("Should get the products with the most stock")
    void getProductsWithMostStockTest() {

        subsidiaryToTest.getProducts().add(productToTest);
        franchiseToTest.getSubsidiaries().clear();
        franchiseToTest.getSubsidiaries().add(subsidiaryToTest);

        when(franchiseRepository.findById(franchiseToTest.getId()))
                .thenReturn(Mono.just(franchiseToTest));

        StepVerifier.create(productUseCaseMock.getProductWithMostStock(franchiseToTest.getId()))
                .expectNextMatches(productWithSubsidiary ->
                    productWithSubsidiaryToTest.getProductName().equals(productToTest.getName()) &&
                    productWithSubsidiaryToTest.getStock().equals(productToTest.getStock()) &&
                    productWithSubsidiaryToTest.getSubsidiaryName().equals(subsidiaryToTest.getName())
                )
                .verifyComplete();


    }
}

