package backend.controller;

import backend.service.UserActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "UserActivityController", description = "Контроллер для сбора и анализа активности пользователей")
@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class UserActivityController {
    private final UserActivityService userActivityService;

    @Operation(summary = "Логирование активности пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Активность успешно залогирована")
    })
    @PostMapping("/log")
    public ResponseEntity<Void> logActivity(
            @RequestBody String userIp,
            @RequestBody String sessionId,
            @RequestBody String pageUrl,
            @RequestBody String eventType,
            @RequestBody String eventDetails,
            @RequestHeader(value = "Referer", required = false) String referer) {

        userActivityService.logActivity(userIp, sessionId, pageUrl, eventType, eventDetails, referer);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение статистики по популярным страницам")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статистика успешно получена")
    })
    @GetMapping("/analytics/popular-pages")
    public ResponseEntity<List<Map<String, Object>>> getPopularPages() {
        return ResponseEntity.ok(userActivityService.getPopularPages());
    }

    @Operation(summary = "Получение статистики по источникам трафика")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статистика успешно получена")
    })
    @GetMapping("/analytics/traffic-sources")
    public ResponseEntity<List<Map<String, Object>>> getTrafficSources() {
        return ResponseEntity.ok(userActivityService.getTrafficSources());
    }

    @Operation(summary = "Получение статистики по времени, проведенному на сайте")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статистика успешно получена")
    })
    @GetMapping("/analytics/time-on-site")
    public ResponseEntity<List<Map<String, Object>>> getTimeOnSite() {
        return ResponseEntity.ok(userActivityService.getTimeOnSite());
    }
}