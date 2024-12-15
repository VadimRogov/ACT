package backend.controller;

import backend.dto.UserActivityLogRequest; // Предполагается, что этот класс существует
import backend.service.UserActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name="UserActivityController",description="Контроллер для сбора и анализа активности пользователей")
@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class UserActivityController {

    private final UserActivityService userActivityService;

    // Логирование активности пользователя
    @Operation(summary="Логирование активности пользователя",description="Логирует активность пользователя на сайте")
    @ApiResponses({
            @ApiResponse(responseCode="200",description="Активность успешно залогирована"),
            @ApiResponse(responseCode="400",description="Некорректные данные запроса")
    })
    @PostMapping("/log")
    public ResponseEntity<Void> logActivity(
            @RequestBody
            @Parameter(description="Данные активности",required=true)
            UserActivityLogRequest request) {

        userActivityService.logActivity(request);
        return ResponseEntity.ok().build();
    }

    // Получение статистики по популярным страницам
    @Operation(summary="Получение статистики по популярным страницам",
            description="Возвращает статистику по популярным страницам")
    @ApiResponses({
            @ApiResponse(responseCode="200",
                    description="Статистика успешно получена",
                    content=@io.swagger.v3.oas.annotations.media.Content(schema=@io.swagger.v3.oas.annotations.media.Schema(implementation=List.class)))
    })
    @GetMapping("/analytics/popular-pages")
    public ResponseEntity<List<Map<String, Object>>> getPopularPages() {
        List<Map<String,Object>> pages=userActivityService.getPopularPages();
        return ResponseEntity.ok(pages);
    }

    // Получение статистики по источникам трафика
    @Operation(summary="Получение статистики по источникам трафика",
            description="Возвращает статистику по источникам трафика")
    @ApiResponses({
            @ApiResponse(responseCode="200",
                    description="Статистика успешно получена",
                    content=@io.swagger.v3.oas.annotations.media.Content(schema=@io.swagger.v3.oas.annotations.media.Schema(implementation=List.class)))
    })
    @GetMapping("/analytics/traffic-sources")
    public ResponseEntity<List<Map<String, Object>>> getTrafficSources() {
        List<Map<String,Object>> sources=userActivityService.getTrafficSources();
        return ResponseEntity.ok(sources);
    }

    // Получение статистики по времени, проведенному на сайте
    @Operation(summary="Получение статистики по времени, проведенному на сайте",
            description="Возвращает статистику по времени, проведенному на сайте")
    @ApiResponses({
            @ApiResponse(responseCode="200",
                    description="Статистика успешно получена",
                    content=@io.swagger.v3.oas.annotations.media.Content(schema=@io.swagger.v3.oas.annotations.media.Schema(implementation=List.class)))
    })
    @GetMapping("/analytics/time-on-site")
    public ResponseEntity<List<Map<String, Object>>> getTimeOnSite() {
        List<Map<String,Object>> timeStats=userActivityService.getTimeOnSite();
        return ResponseEntity.ok(timeStats);
    }
}
