package co.com.accenture.api;

import co.com.accenture.api.docs.CreateFranchiseDocs;
import co.com.accenture.api.docs.CreateSubsidiaryDocs;
import co.com.accenture.api.util.Routes;
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
            @RouterOperation(
                    path = "/api/v1/product/{idFranchise}/{idSubsidiary}",
                    method = RequestMethod.POST,
                    beanClass = CreateSubsidiaryDocs.class,
                    beanMethod = "createSubsidiary"
            ),
            @RouterOperation(
                    path = "/api/v1/product/{franchiseId}/{subsidiaryId}/update-stock",
                    method = RequestMethod.POST,
                    beanClass = CreateSubsidiaryDocs.class,
                    beanMethod = "updateProductStock"
            ),
            @RouterOperation(
                    path = "/api/v1/product/mostStock/{id}",
                    method = RequestMethod.GET,
                    beanClass = CreateSubsidiaryDocs.class,
                    beanMethod = "getProductWithMostStock"
            )

    })
    public RouterFunction<ServerResponse> routerFunction(Handler franchiseHandler) {
        return RouterFunctions
                .route()
                .path(Routes.FRANCHISE, builder -> builder
                        .POST("", franchiseHandler::listenSaveFranchise)
                        .POST("/{id}", franchiseHandler::listenUpdateFranchise)

//                        .GET("", franchiseHandler::listenGetAllFranchises)
                )
                .path(Routes.SUBSIDIARY, builder -> builder
                        .POST("/{id}", franchiseHandler::listenSaveSubsidiary)
                        .POST("/{franchiseId}/{subsidiaryId}", franchiseHandler::listenUpdateSubsidiary)
                )
                .path(Routes.PRODUCT, builder -> builder
                        .POST("/{franchiseId}/{subsidiaryId}", franchiseHandler::listenSaveProduct)
                        .POST("/{franchiseId}/{subsidiaryId}/update-stock", franchiseHandler::listenUpdateProductStock)
                        .GET("/mostStock/{id}", franchiseHandler::listenGetProductWithMostStock)
                )
                .build();
    }
}
