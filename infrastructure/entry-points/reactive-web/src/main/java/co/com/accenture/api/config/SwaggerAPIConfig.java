package co.com.accenture.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Franchises API",
                version = "1.2",
                description = "This API handles the management of franchises applicants",
                contact = @Contact(
                        name = "Juan Villota",
                        email = "juanvillota33@gmail.com",
                        url = "https://github.com/juanvidev/accenture-technical-test"
                )
        )
)
public class SwaggerAPIConfig {
}