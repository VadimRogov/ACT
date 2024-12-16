package backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Разрешить CORS для всех URL
                .allowedOrigins("*") // Разрешить запросы с любого домена
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Разрешенные HTTP-методы
                .allowedHeaders("*") // Разрешенные заголовки
                .allowCredentials(true) // Разрешить передачу куки и авторизационных данных
                .maxAge(3600); // Время кэширования CORS-запросов (в секундах)
    }
}