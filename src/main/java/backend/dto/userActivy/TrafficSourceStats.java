package backend.dto.userActivy;

import lombok.Data;

@Data
public class TrafficSourceStats {
    private String referer; // Источник трафика
    private int visits; // Количество визитов
}
