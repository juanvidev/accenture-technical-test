package co.com.accenture.config;

import co.com.accenture.model.franchise.gateways.FranchiseRepository;
import co.com.accenture.usecase.franchise.FranchiseUseCase;
import co.com.accenture.usecase.product.ProductUseCase;
import co.com.accenture.usecase.subsidiary.SubsidiaryUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.accenture.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {
    @Bean
    FranchiseUseCase franchiseUseCase (
            FranchiseRepository franchiseRepository
    ) {
        return new FranchiseUseCase(franchiseRepository);
    }

    @Bean
    SubsidiaryUseCase subsidiaryUseCase (FranchiseRepository franchiseRepository) {
        return new SubsidiaryUseCase(franchiseRepository);
    }

    @Bean
    ProductUseCase productUseCase (FranchiseRepository franchiseRepository) {
        return new ProductUseCase(franchiseRepository);
    }
}
