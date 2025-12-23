package co.com.accenture.api;

import co.com.accenture.api.docs.CreateFranchiseDocs;
import co.com.accenture.api.docs.CreateSubsidiaryDocs;
import co.com.accenture.api.util.Routes;
import co.com.accenture.model.franchise.Franchise;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Tag(name = "Franchises", description = "Router configuration for Franchise, Subsidiary and Products endpoints")
public class RouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/franchise",
                    method = RequestMethod.POST,
                    beanClass = CreateFranchiseDocs.class,
                    beanMethod = "createFranchise"
            ),
            @RouterOperation(
                    path = "/api/v1/subsidiary/{id}",
                    method = RequestMethod.POST,
                    beanClass = CreateSubsidiaryDocs.class,
                    beanMethod = "createSubsidiary"
            ),

    })
    public RouterFunction<ServerResponse> routerFunction(Handler franchiseHandler) {
        return RouterFunctions
                .route()
                .path(Routes.FRANCHISE, builder -> builder
                        .POST("", franchiseHandler::listenSaveFranchise)
                )
                .path(Routes.SUBSIDIARY, builder -> builder
                        .POST("/{id}", franchiseHandler::listenSaveSubsidiary)
                )
                .build();
    }
}
