package backend.controller;

import backend.dto.UserActivityLogRequest;
import backend.dto.userActivy.PageStats;
import backend.dto.userActivy.TrafficSourceStats;
import backend.dto.userActivy.TimeOnSiteStats;
import backend.service.UserActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "UserActivityController", description = "Контроллер для сбора и анализа активности пользователей")
@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class UserActivityController {

    private final UserActivityService userActivityService;

    // Логирование активности пользователя
    @Operation(summary = "Логирование активности пользователя", description = "Логирует активность пользователя на сайте")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Активность успешно залогирована"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    @PostMapping("/log")
    public ResponseEntity<Void> logActivity(
            @RequestBody
            @Parameter(description = "Данные активности", required = true,
                    content = @Content(schema = @Schema(implementation = UserActivityLogRequest.class)))
            UserActivityLogRequest request) {
        userActivityService.logActivity(request);
        return ResponseEntity.ok().build();
    }

    // Получение статистики по популярным страницам
    @Operation(summary = "Получение статистики по популярным страницам", description = "Возвращает статистику по популярным страницам")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статистика успешно получена",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PageStats.class))))
    })
    @GetMapping("/analytics/popular-pages")
    public ResponseEntity<List<PageStats>> getPopularPages() {
        List<PageStats> pages = userActivityService.getPopularPages();
        return ResponseEntity.ok(pages);
    }

    // Получение статистики по источникам трафика
    @Operation(summary = "Получение статистики по источникам трафика", description = "Возвращает статистику по источникам трафика")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статистика успешно получена",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TrafficSourceStats.class))))
    })
    @GetMapping("/analytics/traffic-sources")
    public ResponseEntity<List<TrafficSourceStats>> getTrafficSources() {
        List<TrafficSourceStats> sources = userActivityService.getTrafficSources();
        return ResponseEntity.ok(sources);
    }

    // Получение статистики по времени, проведенному на сайте
    @Operation(summary = "Получение статистики по времени, проведенному на сайте", description = "Возвращает статистику по времени, проведенному на сайте")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статистика успешно получена",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TimeOnSiteStats.class))))
    })
    @GetMapping("/analytics/time-on-site")
    public ResponseEntity<List<TimeOnSiteStats>> getTimeOnSite() {
        List<TimeOnSiteStats> timeStats = userActivityService.getTimeOnSite();
        return ResponseEntity.ok(timeStats);
    }
}