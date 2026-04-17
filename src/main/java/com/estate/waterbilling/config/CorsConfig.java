package com.estate.waterbilling.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")

                        // Allow ALL frontends → solves 100% of issues
                        .allowedOrigins("*")

                        // Allow all common HTTP methods
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

                        // Allow all headers
                        .allowedHeaders("*")

                        // Must disable credentials when using wildcard (*)
                        .allowCredentials(false);
            }
        };
    }
}
