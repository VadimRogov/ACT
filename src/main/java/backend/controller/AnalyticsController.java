package backend.controller;

import backend.dto.analytics.InteractiveElementStats;
import backend.dto.analytics.PageStatsAnalytics;
import backend.dto.analytics.TimeOnSiteStatsAnalytics;
import backend.dto.analytics.TrafficSourceStats;
import backend.security.JwtUtil;
import backend.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Tag(name = "AnalyticsController", description = "Контроллер для предоставления аналитики")
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    private final JwtUtil jwtUtil;

    public AnalyticsController(AnalyticsService analyticsService, JwtUtil jwtUtil) {
        this.analyticsService = analyticsService;
        this.jwtUtil = jwtUtil;
    }


    @Operation(summary = "Получить количество уникальных посетителей", description = "Возвращает количество уникальных посетителей сайта")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные успешно получены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class, example = "{\"count\": 123}"))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/unique-visitors")
    public ResponseEntity<Map<String, Long>> getUniqueVisitorsCount(
            @RequestHeader("Authorization") @Parameter(description = "Токен авторизации", required = true) String authorizationHeader) {

        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        if ("admin".equals(username)) {
            Long count = analyticsService.getUniqueVisitorsCount();
            return ResponseEntity.ok(Map.of("count", count));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    @Operation(summary = "Получить источники трафика", description = "Возвращает список источников трафика")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные успешно получены",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TrafficSourceStats.class)))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/traffic-sources")
    public ResponseEntity<List<TrafficSourceStats>> getTrafficSources(
            @RequestHeader("Authorization") @Parameter(description = "Токен авторизации", required = true) String authorizationHeader) {

        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        if ("admin".equals(username)) {
            return ResponseEntity.ok(analyticsService.getTrafficSources());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }



    @Operation(summary = "Получить время, проведенное на сайте", description = "Возвращает статистику по времени, проведенному на сайте")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные успешно получены",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TimeOnSiteStatsAnalytics.class)))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/time-on-site")
    public ResponseEntity<List<TimeOnSiteStatsAnalytics>> getTimeOnSite(
            @RequestHeader("Authorization") @Parameter(description = "Токен авторизации", required = true) String authorizationHeader) {

        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        if ("admin".equals(username)) {
            return ResponseEntity.ok(analyticsService.getTimeOnSite());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }



    @Operation(summary = "Получить популярные страницы", description = "Возвращает список популярных страниц")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные успешно получены",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PageStatsAnalytics.class)))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/popular-pages")
    public ResponseEntity<List<PageStatsAnalytics>> getPopularPages(
            @RequestHeader("Authorization") @Parameter(description = "Токен авторизации", required = true) String authorizationHeader) {

        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        if ("admin".equals(username)) {
            return ResponseEntity.ok(analyticsService.getPopularPages());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }



    @Operation(summary = "Получить взаимодействия с интерактивными элементами", description = "Возвращает статистику по взаимодействиям с интерактивными элементами")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные успешно получены",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = InteractiveElementStats.class)))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/interactive-elements")
    public ResponseEntity<List<InteractiveElementStats>> getInteractiveElementInteractions(
            @RequestHeader("Authorization") @Parameter(description = "Токен авторизации", required = true) String authorizationHeader) {

        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        if ("admin".equals(username)) {
            return ResponseEntity.ok(analyticsService.getInteractiveElementInteractions());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}