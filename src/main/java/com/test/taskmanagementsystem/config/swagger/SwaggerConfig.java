package com.test.taskmanagementsystem.config.swagger;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Management System API")
                        .description("Task management system api specification")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Dmitry")
                                .email("https://dmitridorje@gmail.com/"))
                        .license(new License()
                                .name("License name")
                                .url("https://some-url.com"))
                        .termsOfService("Terms of service"))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Local Environment"));
    }
}
