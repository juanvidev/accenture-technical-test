package co.com.accenture.api;

import co.com.accenture.api.util.Routes;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/franchise",
                    method = RequestMethod.POST,
                    beanMethod = "createFranchise"
            ),
    })
    public RouterFunction<ServerResponse> routerFunction(Handler franchiseHandler) {
        return RouterFunctions
                .route()
                .path(Routes.FRANCHISE, builder -> builder
                        .POST("", franchiseHandler::listenSaveFranchise)
//                        .GET("", userHandler::listenExistsByEmailAndDocumentId)
                )
                .path(Routes.SUBSIDIARY, builder -> builder
                        .POST("/{id}", franchiseHandler::listenSaveSubsidiary)
                )
                .build();
    }
}
