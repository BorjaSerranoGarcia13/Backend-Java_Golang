package com.ecommerce;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;

@OpenAPIDefinition(info = @Info(title = "Ecommerce API", version = "v1", description = "API for Ecommerce Application"))
@SpringBootApplication
public class SpringEcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringEcommerceApplication.class, args);
    }

}
