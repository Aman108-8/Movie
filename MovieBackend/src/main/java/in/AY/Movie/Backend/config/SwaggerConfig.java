package in.AY.Movie.Backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContext;

@Configuration
public class SwaggerConfig {
	
	public static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI movieOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                		.title("Movie API")
                        .description("Movie Backend APIs code")
                        .version("1.0")
                        .contact(new Contact()
                        .name("AY")
                        .email("AYTech@email.com")))
                		.addSecurityItem(new SecurityRequirement()
                        .addList(SECURITY_SCHEME_NAME))
                		.components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
	                        new SecurityScheme()
	                        .name(SECURITY_SCHEME_NAME)
	                        .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")));
    }
}
