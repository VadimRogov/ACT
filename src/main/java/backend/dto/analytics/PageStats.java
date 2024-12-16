package backend.dto.analytics;

import lombok.Data;

@Data
public class PageStats {
    private String pageUrl; // URL страницы
    private int views; // Количество просмотров
}