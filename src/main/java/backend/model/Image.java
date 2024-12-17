package backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id", nullable = false)
    private Book bookImage; // Связь с книгой

    @OneToOne
    @JoinColumn(name = "cover_id", referencedColumnName = "id", nullable = false)
    private Book bookCover;

    @Lob
    @Column(name = "image_data", nullable = false)
    private byte[] imageData; // Данные изображения в виде массива байтов

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type", nullable = false)
    private ImageType imageType; // Тип изображения

    // Приватный конструктор для использования Builder
    private Image(Builder builder) {
        this.bookImage = builder.bookImage;
        this.bookCover = builder.bookCover;
        this.imageData = builder.imageData;
        this.imageType = builder.imageType;
    }
    public Image() {

    }

    public Image(Long id, Book bookImage, Book bookCover, byte[] imageData, ImageType imageType) {
        this.id = id;
        this.bookImage = bookImage;
        this.bookCover = bookCover;
        this.imageData = imageData;
        this.imageType = imageType;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBookImage() {
        return bookImage;
    }

    public void setBookImage(Book bookImage) {
        this.bookImage = bookImage;
    }

    public Book getBookCover() {
        return bookCover;
    }

    public void setBookCover(Book bookCover) {
        this.bookCover = bookCover;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    // Вложенный класс Builder
    public static class Builder {
        private Book bookImage;
        private Book bookCover;
        private byte[] imageData;
        private ImageType imageType;

        // Методы для установки значений
        public Builder bookImage(Book bookImage) {
            this.bookImage = bookImage;
            return this;
        }

        public Builder bookCover(Book bookCover) {
            this.bookCover = bookCover;
            return this;
        }

        public Builder imageData(byte[] imageData) {
            this.imageData = imageData;
            return this;
        }

        public Builder imageType(ImageType imageType) {
            this.imageType = imageType;
            return this;
        }

        // Метод для создания объекта Image
        public Image build() {
            return new Image(this);
        }
    }

    // Метод для создания экземпляра Builder
    public static Builder builder() {
        return new Builder();
    }
}