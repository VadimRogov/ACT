package backend.config;

import backend.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Отключаем CSRF, так как используем JWT
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Отключаем сессии
                .and()
                .authorizeHttpRequests() // Настройка авторизации запросов
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/backend/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated(); // Все остальные запросы требуют аутентификации

        // Добавляем фильтр для обработки JWT перед UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}