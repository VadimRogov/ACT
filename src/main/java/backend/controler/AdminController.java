package backend.controller;

import backend.security.JwtUtil;
import backend.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "AdminController", description = "Контроллер для авторизации администратора и смены пароля")
@RestController
@RequestMapping("/api/auth")
public class AdminController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;

    public AdminController(AdminService adminService, JwtUtil jwtUtil) {
        this.adminService = adminService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Вход администратора")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный вход"),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Parameter(description = "Имя пользователя", required = true) @RequestBody String username,
            @Parameter(description = "Пароль", required = true) @RequestBody String password) {

        if (adminService.authenticate(username, password)) {
            String token = jwtUtil.generateToken(username);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @Operation(summary = "Смена пароля администратора")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пароль успешно изменен"),
            @ApiResponse(responseCode = "403", description = "Старый пароль неверен")
    })
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @Parameter(description = "Имя пользователя", required = true) @RequestBody String username,
            @Parameter(description = "Старый пароль", required = true) @RequestBody String oldPassword,
            @Parameter(description = "Новый пароль", required = true) @RequestBody String newPassword) {

        if (adminService.changePassword(username, oldPassword, newPassword)) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(403).body("Incorrect old password");
        }
    }
}