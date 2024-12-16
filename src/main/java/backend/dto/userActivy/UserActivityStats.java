package backend.dto.userActivy;

import lombok.Data;

@Data
public class UserActivityStats {
    private String key; // Ключ, представляющий уникальный идентификатор (например, URL страницы или источник трафика)
    private int value; // Значение, представляющее количество просмотров или визитов
}
