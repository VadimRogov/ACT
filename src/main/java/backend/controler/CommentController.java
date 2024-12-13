package backend.controller;

import backend.model.Comment;
import backend.security.JwtUtil;
import backend.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
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
            @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @PostMapping
    public ResponseEntity<Comment> addComment(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String content,
            @RequestParam String author) {

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
            @ApiResponse(responseCode = "200", description = "Комментарий успешно обновлен"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id,
            @RequestParam String content,
            @RequestParam String author) {

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
            @ApiResponse(responseCode = "204", description = "Комментарий успешно удален"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id) {

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
            @ApiResponse(responseCode = "200", description = "Комментарии успешно получены"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
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