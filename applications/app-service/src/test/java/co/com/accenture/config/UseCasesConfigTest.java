package co.com.accenture.config;

import co.com.accenture.model.franchise.gateways.FranchiseRepository;
import co.com.accenture.usecase.franchise.FranchiseUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class UseCasesConfigTest {

    @Test
    void testFranchiseUseCaseTest() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            assertNotNull(context.getBean(FranchiseUseCase.class), "Franchise bean should be loaded in the context");

        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        FranchiseRepository franchiseRepository() {
            return mock(FranchiseRepository.class);
        }
    }

}