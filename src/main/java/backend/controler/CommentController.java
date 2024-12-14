package backend.controller;

import backend.model.Comment;
import backend.security.JwtUtil;
import backend.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "CommentController", description = "Контроллер для работы с комментариями")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Добавить комментарий")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Комментарий успешно добавлен",
                    content = @Content(schema = @Schema(implementation = Comment.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Comment> addComment(
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "Содержимое комментария") @RequestBody String content,
            @Parameter(description = "Автор комментария") @RequestBody String author) {

        // Извлекаем токен из заголовка
        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        // Проверяем, что пользователь авторизован
        if ("admin".equals(username)) {
            return ResponseEntity.ok(commentService.addComment(content, author));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Обновить комментарий")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Комментарий успешно обновлен",
                    content = @Content(schema = @Schema(implementation = Comment.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "Комментарий не найден",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "ID комментария") @PathVariable Long id,
            @Parameter(description = "Новое содержимое комментария") @RequestBody String content,
            @Parameter(description = "Новый автор комментария") @RequestBody String author) {

        // Извлекаем токен из заголовка
        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        // Проверяем, что пользователь авторизован
        if ("admin".equals(username)) {
            return ResponseEntity.ok(commentService.updateComment(id, content, author));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Удалить комментарий")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Комментарий успешно удален",
                    content = @Content),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "Комментарий не найден",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "ID комментария") @PathVariable Long id) {

        // Извлекаем токен из заголовка
        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        // Проверяем, что пользователь авторизован
        if ("admin".equals(username)) {
            commentService.deleteComment(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Получить все комментарии")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Комментарии успешно получены",
                    content = @Content(schema = @Schema(implementation = Comment.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments(
            @RequestHeader("Authorization") String authorizationHeader) {

        // Извлекаем токен из заголовка
        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        // Проверяем, что пользователь авторизован
        if ("admin".equals(username)) {
            return ResponseEntity.ok(commentService.getAllComments());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}