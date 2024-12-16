package backend.controller;

import backend.model.Book;
import backend.model.ImageType;
import backend.service.BookService;
import backend.service.ImageService;
import backend.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "ImageController", description = "Контроллер для работы с изображениями")
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final BookService bookService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Сохранение изображения", description = "Сохраняет новое изображение для книги")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Изображение успешно сохранено"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createImage(
            @RequestHeader("Authorization") @Parameter(description = "Токен авторизации", required = true) String authorizationHeader,
            @RequestParam("bookId") @Parameter(description = "ID книги", required = true) Long bookId,
            @RequestParam("imageType") @Parameter(description = "Тип изображения", required = true) ImageType imageType,
            @RequestParam("file") @Parameter(description = "Файл изображения", required = true) MultipartFile file) {

        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        if ("admin".equals(username)) {
            Book book = bookService.getBookById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            return ResponseEntity.ok(imageService.createImage(book, imageType, file));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Обновление изображения", description = "Обновляет существующее изображение для книги")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Изображение успешно обновлено"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Изображение не найдено")
    })
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(
            @RequestHeader("Authorization") @Parameter(description = "Токен авторизации", required = true) String authorizationHeader,
            @RequestParam("bookId") @Parameter(description = "ID книги", required = true) Long bookId,
            @RequestParam("imageType") @Parameter(description = "Тип изображения", required = true) ImageType imageType,
            @RequestParam("file") @Parameter(description = "Файл изображения", required = true) MultipartFile file) {

        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        if ("admin".equals(username)) {
            Book book = bookService.getBookById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            return ResponseEntity.ok(imageService.updateImage(book, imageType, file));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Удаление изображения", description = "Удаляет изображение для книги")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Изображение успешно удалено"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Изображение не найдено")
    })
    @DeleteMapping("/{bookId}/{imageType}")
    public ResponseEntity<Void> deleteImage(
            @RequestHeader("Authorization") @Parameter(description = "Токен авторизации", required = true) String authorizationHeader,
            @PathVariable("bookId") @Parameter(description = "ID книги", required = true) Long bookId,
            @PathVariable("imageType") @Parameter(description = "Тип изображения", required = true) ImageType imageType) {

        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        if ("admin".equals(username)) {
            Book book = bookService.getBookById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            imageService.deleteImage(book, imageType);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Получение изображения", description = "Возвращает изображение для книги по указанному ID и типу")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Изображение успешно получено"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Изображение не найдено")
    })
    @GetMapping("/{bookId}/{imageType}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(
            @RequestHeader("Authorization") @Parameter(description = "Токен авторизации", required = true) String authorizationHeader,
            @PathVariable("bookId") @Parameter(description = "ID книги", required = true) Long bookId,
            @PathVariable("imageType") @Parameter(description = "Тип изображения", required = true) ImageType imageType) {

        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        if ("admin".equals(username)) {
            Book book = bookService.getBookById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            byte[] imageData = imageService.getImageByBookAndType(book, imageType);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageData);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}