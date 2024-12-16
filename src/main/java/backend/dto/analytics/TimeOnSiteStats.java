package backend.dto.analytics;

import lombok.Data;

@Data
public class TimeOnSiteStats {
    private String sessionId; // Идентификатор сессии
    private long timeOnSite; // Время на сайте в секундах
}