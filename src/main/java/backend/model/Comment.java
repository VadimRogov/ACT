package backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Уникальный идентификатор комментария

    @Column(name = "content", nullable = false)
    private String content; // Текст комментария

    @Column(name = "author", nullable = false)
    private String author; // Автор комментария
}