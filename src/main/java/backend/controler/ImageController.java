package backend.controller;

import backend.model.Image;
import backend.model.ImageType;
import backend.service.ImageService;
import backend.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "ImageController", description = "Контроллер сохранения, обновления, получения и удаления изображений")
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Сохранение изображения")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Изображение успешно сохранено",
                    content = @Content(schema = @Schema(implementation = Image.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к запрошенному ресурсу запрещен",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createImage(
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "Id книги") @RequestParam("bookId") Long bookId,
            @Parameter(description = "Тип изображения") @RequestParam("imageType") ImageType imageType,
            @Parameter(description = "Файл изображения") @RequestParam("file") MultipartFile file) {

        // Извлекаем токен из заголовка
        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        // Проверяем, что пользователь авторизован (например, проверяем, что username соответствует администратору)
        if ("admin".equals(username)) {
            return ResponseEntity.ok(imageService.createImage(bookId, imageType, file));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Обновление изображения")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Изображение успешно обновлено",
                    content = @Content(schema = @Schema(implementation = Image.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к запрошенному ресурсу запрещен",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "Изображение не найдено",
                    content = @Content)
    })
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "Id книги") @RequestParam("bookId") Long bookId,
            @Parameter(description = "Тип изображения") @RequestParam("imageType") ImageType imageType,
            @Parameter(description = "Файл изображения") @RequestParam("file") MultipartFile file) {

        // Извлекаем токен из заголовка
        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        // Проверяем, что пользователь авторизован (например, проверяем, что username соответствует администратору)
        if ("admin".equals(username)) {
            return ResponseEntity.ok(imageService.updateImage(bookId, imageType, file));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Удаление изображения")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Изображение успешно удалено",
                    content = @Content),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к запрошенному ресурсу запрещен",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "Изображение не найдено",
                    content = @Content)
    })
    @DeleteMapping("/{bookId}/{imageType}")
    public ResponseEntity<Void> deleteImage(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("bookId") Long bookId,
            @PathVariable("imageType") ImageType imageType) {

        // Извлекаем токен из заголовка
        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        // Проверяем, что пользователь авторизован (например, проверяем, что username соответствует администратору)
        if ("admin".equals(username)) {
            imageService.deleteImage(bookId, imageType);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Получение изображения по id книги и типу")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный запрос",
                    content = @Content(mediaType = MediaType.IMAGE_PNG_VALUE)),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к запрошенному ресурсу запрещен",
                    content = @Content)
    })
    @GetMapping("/{bookId}/{imageType}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("bookId") Long bookId,
            @PathVariable("imageType") ImageType imageType) {

        // Извлекаем токен из заголовка
        String token = authorizationHeader.substring(7); // Убираем "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        // Проверяем, что пользователь авторизован (например, проверяем, что username соответствует администратору)
        if ("admin".equals(username)) {
            byte[] imageData = imageService.getImageByBookIdAndType(bookId, imageType);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG) // Укажите правильный тип контента
                    .body(imageData);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}