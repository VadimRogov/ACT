package backend.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteractiveElementStats {
    private String elementId; // Идентификатор элемента
    private int interactions; // Количество взаимодействий
}