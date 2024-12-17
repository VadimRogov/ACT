package backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity")
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

    public UserActivity() {

    }

    // Приватный конструктор для использования Builder
    private UserActivity(Builder builder) {
        this.userIp = builder.userIp;
        this.sessionId = builder.sessionId;
        this.pageUrl = builder.pageUrl;
        this.eventType = builder.eventType;
        this.eventDetails = builder.eventDetails;
        this.referer = builder.referer;
        this.timestamp = builder.timestamp;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Вложенный класс Builder
    public static class Builder {
        private String userIp;
        private String sessionId;
        private String pageUrl;
        private String eventType;
        private String eventDetails;
        private String referer;
        private LocalDateTime timestamp;

        // Методы для установки значений
        public Builder userIp(String userIp) {
            this.userIp = userIp;
            return this;
        }

        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder pageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
            return this;
        }

        public Builder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder eventDetails(String eventDetails) {
            this.eventDetails = eventDetails;
            return this;
        }

        public Builder referer(String referer) {
            this.referer = referer;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        // Метод для создания объекта UserActivity
        public UserActivity build() {
            return new UserActivity(this);
        }
    }

    // Метод для создания экземпляра Builder
    public static Builder builder() {
        return new Builder();
    }
}