package backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "url", nullable = false)
    private String url;

    @OneToOne(mappedBy = "bookCover", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cover_image_id")
    private Image cover;

    @OneToMany(mappedBy = "bookImage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> bookImage;

    public Book() {

    }

    public Book(String title, String author, String description, String url, Image cover, List<Image> bookImage) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.url = url;
        this.cover = cover;
        this.bookImage = bookImage;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Image getCover() {
        return cover;
    }

    public void setCover(Image cover) {
        this.cover = cover;
    }

    public List<Image> getBookImage() {
        return bookImage;
    }

    public void setBookImage(List<Image> bookImage) {
        this.bookImage = bookImage;
    }
}
