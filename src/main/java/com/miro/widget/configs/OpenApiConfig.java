package com.miro.widget.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(new Info().title("Widgets API").description(
                "A web service to work with widgets via HTTP REST API. The service stores only widgets,\n" +
                    "assuming that all clients work with the same board.\n" +
                    "A Widget is an object on a plane in a Cartesian coordinate system that has coordinates (X, Y),\n" +
                    "Z-index, width, height, last modification date, and a unique identifier . X, Y, and Z-index are\n" +
                    "integers (may be negative). Width and height are integers > 0.\n" +
                    "Widget attributes should be not null.\n" +
                    "A Z-index is a unique sequence common to all widgets that\n" +
                    "determines the order of widgets (regardless of their coordinates).\n" +
                    "Gaps are allowed. The higher the value, the higher the widget\n" +
                    "lies on the plane."));
    }
}
