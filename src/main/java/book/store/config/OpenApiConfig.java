package book.store.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI myOpenApi() {
        Contact contact = new Contact();
        contact.setUrl("https://www.bookstore.com");

        Info info = new Info()
                .title("Book Store Management API")
                .contact(contact)
                .description("This API exposes endpoints to manage book store.");

        return new OpenAPI().info(info);
    }
}
