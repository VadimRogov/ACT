package backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_ip")
    private String userIp; // IP-адрес пользователя

    @Column(name = "session_id")
    private String sessionId; // Идентификатор сессии

    @Column(name = "page_url")
    private String pageUrl; // URL страницы

    @Column(name = "event_type")
    private String eventType; // Тип события (например, "view", "click", "exit")

    @Column(name = "event_details")
    private String eventDetails; // Дополнительные данные о событии

    @Column(name = "referer")
    private String referer; // Источник трафика (откуда пользователь перешел)

    @Column(name = "timestamp")
    private LocalDateTime timestamp; // Время события
}