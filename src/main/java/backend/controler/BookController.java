package backend.controller;

import backend.model.Book;
import backend.security.JwtUtil;
import backend.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "BookController", description = "Контроллер для работы с книгами")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final JwtUtil jwtUtil;

    public BookController(BookService bookService, JwtUtil jwtUtil) {
        this.bookService = bookService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Получить все книги")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Книги успешно получены")
    })
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @Operation(summary = "Получить книгу по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Книга успешно получена"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Добавить книгу")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Книга успешно добавлена"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Book book) {
        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        // Проверяем, что пользователь авторизован (например, проверяем, что username соответствует администратору)
        if ("admin".equals(username)) {
            return ResponseEntity.ok(bookService.addBook(book));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @Operation(summary = "Удалить книгу по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Книга успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        // Проверяем, что пользователь авторизован (например, проверяем, что username соответствует администратору)
        if ("admin".equals(username)) {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}