package backend.dto.analytics;

import lombok.Data;

@Data
public class TrafficSourceStats {
    private String source; // Источник трафика
    private int visits; // Количество визитов
}