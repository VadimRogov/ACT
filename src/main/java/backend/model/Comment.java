package backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Уникальный идентификатор комментария

    @Column(name = "content", nullable = false)
    private String content; // Текст комментария

    @Column(name = "author", nullable = false)
    private String author; // Автор комментария

    public Comment() {

    }

    // Приватный конструктор для использования Builder
    private Comment(Builder builder) {
        this.content = builder.content;
        this.author = builder.author;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // Вложенный класс Builder
    public static class Builder {
        private String content;
        private String author;

        // Методы для установки значений
        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        // Метод для создания объекта Comment
        public Comment build() {
            return new Comment(this);
        }
    }

    // Метод для создания экземпляра Builder
    public static Builder builder() {
        return new Builder();
    }
}