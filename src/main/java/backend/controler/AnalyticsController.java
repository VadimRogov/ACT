package backend.controller;

import backend.security.JwtUtil;
import backend.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "AnalyticsController", description = "Контроллер для предоставления аналитики")
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Получить количество уникальных посетителей")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные успешно получены"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/unique-visitors")
    public ResponseEntity<Long> getUniqueVisitorsCount(
            @RequestHeader("Authorization") String authorizationHeader) {

        // Извлекаем токен из заголовка
        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        // Проверяем, что пользователь авторизован (например, проверяем, что username соответствует администратору)
        if ("admin".equals(username)) {
            return ResponseEntity.ok(analyticsService.getUniqueVisitorsCount());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Получить источники трафика")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные успешно получены"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/traffic-sources")
    public ResponseEntity<List<Map<String, Object>>> getTrafficSources(
            @RequestHeader("Authorization") String authorizationHeader) {

        // Извлекаем токен из заголовка
        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        // Проверяем, что пользователь авторизован (например, проверяем, что username соответствует администратору)
        if ("admin".equals(username)) {
            return ResponseEntity.ok(analyticsService.getTrafficSources());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Получить время, проведенное на сайте")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные успешно получены"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/time-on-site")
    public ResponseEntity<List<Map<String, Object>>> getTimeOnSite(
            @RequestHeader("Authorization") String authorizationHeader) {

        // Извлекаем токен из заголовка
        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        // Проверяем, что пользователь авторизован (например, проверяем, что username соответствует администратору)
        if ("admin".equals(username)) {
            return ResponseEntity.ok(analyticsService.getTimeOnSite());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Получить популярные страницы")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные успешно получены"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/popular-pages")
    public ResponseEntity<List<Map<String, Object>>> getPopularPages(
            @RequestHeader("Authorization") String authorizationHeader) {

        // Извлекаем токен из заголовка
        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        // Проверяем, что пользователь авторизован (например, проверяем, что username соответствует администратору)
        if ("admin".equals(username)) {
            return ResponseEntity.ok(analyticsService.getPopularPages());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Получить взаимодействия с интерактивными элементами")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные успешно получены"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/interactive-elements")
    public ResponseEntity<List<Map<String, Object>>> getInteractiveElementInteractions(
            @RequestHeader("Authorization") String authorizationHeader) {

        // Извлекаем токен из заголовка
        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        // Проверяем, что пользователь авторизован (например, проверяем, что username соответствует администратору)
        if ("admin".equals(username)) {
            return ResponseEntity.ok(analyticsService.getInteractiveElementInteractions());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}