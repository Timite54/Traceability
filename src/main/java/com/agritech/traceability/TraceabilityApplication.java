package com.agritech.traceability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "API de Traçabilité Agricole Blockchain",
        version = "1.0",
        description = "API pour la traçabilité des produits agricoles utilisant la technologie blockchain"
    )
)
public class TraceabilityApplication{
    public static void main(String[] args) {
        SpringApplication.run(TraceabilityApplication.class, args);
    }
}