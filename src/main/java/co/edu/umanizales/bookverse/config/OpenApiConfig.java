package co.edu.umanizales.bookverse.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI (Swagger) configuration for REST API documentation
 */
@Configuration
public class OpenApiConfig {

    @Value("${bookverse.openapi.dev-url:http://localhost:8080}")
    private String devUrl;

    @Bean
    public OpenAPI bookVerseOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Development Server");

        Contact contact = new Contact();
        contact.setName("BookVerse Team");
        contact.setEmail("contact@bookverse.com");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("BookVerse API")
                .version("1.0.0")
                .contact(contact)
                .description("API REST para sistema de gestión de librería digital BookVerse. " +
                        "Permite administrar catálogo de libros, autores, clientes, empleados y pedidos.")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}
