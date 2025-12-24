package co.com.accenture.api;

import co.com.accenture.api.dto.request.CreateFranchiseRequestDTO;
import co.com.accenture.api.dto.response.CreateFranchiseResponseDTO;
import co.com.accenture.api.exception.ConstraintViolationException;
import co.com.accenture.api.exception.GlobalExceptionHandler;
import co.com.accenture.api.exception.UnexpectedErrorException;
import co.com.accenture.api.mapper.FranchiseMapper;
import co.com.accenture.api.mapper.ProductMapper;
import co.com.accenture.api.mapper.SubsidiaryMapper;
import co.com.accenture.api.util.ValidatorUtil;
import co.com.accenture.model.exception.BusinessException;
import co.com.accenture.model.franchise.Franchise;
import co.com.accenture.usecase.franchise.FranchiseUseCase;
import co.com.accenture.usecase.product.ProductUseCase;
import co.com.accenture.usecase.subsidiary.SubsidiaryUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {
        RouterRest.class,
        Handler.class,
        GlobalExceptionHandler.class,
})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private FranchiseUseCase franchiseUseCase;
    @MockitoBean
    private SubsidiaryUseCase subsidiaryUseCase;
    @MockitoBean
    private ProductUseCase productUseCase;
    @MockitoBean
    private FranchiseMapper franchiseMapper;
    @MockitoBean
    private SubsidiaryMapper subsidiaryMapper;
    @MockitoBean
    private ProductMapper productMapper;
    @MockitoBean
    private ValidatorUtil validatorUtil;


    CreateFranchiseRequestDTO createFranchiseRequestDTO;
    CreateFranchiseResponseDTO createFranchiseResponseDTO;

    @BeforeEach
    void setUp() {
        Franchise franchiseToTest = new Franchise();
        franchiseToTest.setName("Subway");


        createFranchiseRequestDTO = new CreateFranchiseRequestDTO(
                        franchiseToTest.getName(),
                        new ArrayList<>(List.of())
        );

        createFranchiseResponseDTO = new CreateFranchiseResponseDTO(
                franchiseToTest.getId(),
                franchiseToTest.getName()
        );

        when(validatorUtil.validate(any(CreateFranchiseRequestDTO.class)))
                .thenReturn(Mono.just(createFranchiseRequestDTO));

        when(franchiseMapper.toDomain(any(CreateFranchiseRequestDTO.class)))
                .thenReturn(franchiseToTest);

        when(franchiseUseCase.saveFranchise(any(Franchise.class)))
                .thenReturn(Mono.just(franchiseToTest));

    }


    @Test
    @DisplayName("Should be return 201 when create franchise successfully")
    void testPostFranchiseSuccessfully() {
        webTestClient.post()
                .uri("/api/v1/franchise")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFranchiseRequestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data.name").isEqualTo(createFranchiseResponseDTO.name());


    }

    @Test
    @DisplayName("Should be return 400 when request is invalid")
    void testPostUFranchiseWithInvalidRequest() {
        BusinessException bussinessException = new BusinessException(
                "400",
                "A franchise with the same name already exists."
        );

        when(validatorUtil.validate(any(CreateFranchiseRequestDTO.class)))
                .thenReturn(Mono.error(bussinessException));

        webTestClient.post()
                .uri("/api/v1/franchise")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFranchiseRequestDTO)
                .exchange()
                .expectStatus().isEqualTo(Integer.parseInt(bussinessException.getErrorCode()))
                .expectBody()
                .jsonPath("$.errorCode").isEqualTo("400");
    }

    @Test
    @DisplayName("Should be return 500 when unexpected error occurs")
    void testWhenUnexpectedErrorOccurs() {
        UnexpectedErrorException unexpectedError = new UnexpectedErrorException(new RuntimeException());

        when(validatorUtil.validate(any(CreateFranchiseRequestDTO.class)))
                .thenReturn(Mono.error(unexpectedError));

        webTestClient.post()
                .uri("/api/v1/franchise")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFranchiseRequestDTO)
                .exchange()
                .expectStatus().isEqualTo(unexpectedError.getStatusCode())
                .expectBody()
                .jsonPath("$.errorCode").isEqualTo("UNEXPECTED_ERROR");
    }

    @Test
    @DisplayName("Should be validation error when request has missing fields")
    void testPostFranchiseWithMissingFields() {
        ConstraintViolationException validationException = new ConstraintViolationException(
                List.of("Name is required")
        );

        when(validatorUtil.validate(any(CreateFranchiseRequestDTO.class)))
                .thenReturn(Mono.error(validationException));

        webTestClient.post()
                .uri("/api/v1/franchise")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFranchiseRequestDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errorCode").isEqualTo("CONSTRAINT_VIOLATION")
                .jsonPath("$.errors[0]").isEqualTo("Name is required");
    }

    @Test
    @DisplayName("Should be return 500 when unexpected in system")
    void testWhenRuntimeException() {
        RuntimeException runtimeException = new RuntimeException();

        when(validatorUtil.validate(any(CreateFranchiseRequestDTO.class)))
                .thenReturn(Mono.error(runtimeException));

        webTestClient.post()
                .uri("/api/v1/franchise")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFranchiseRequestDTO)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.errorCode").isEqualTo("UNEXPECTED_ERROR");
    }

    @Test
    @DisplayName("Should be ilegal argument exception when exists business error")
    void testWhenIllegalArgumentException() {

        when(validatorUtil.validate(any(CreateFranchiseRequestDTO.class)))
                .thenReturn(Mono.error(new BusinessException("BSS_002", "Franchise already exists")));

        webTestClient.post()
                .uri("/api/v1/franchise")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFranchiseRequestDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Franchise already exists");
    }

}
