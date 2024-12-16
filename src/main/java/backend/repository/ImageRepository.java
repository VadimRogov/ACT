package backend.repository;

import backend.model.Book;
import backend.model.Image;
import backend.model.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByBookAndImageType(Book book, ImageType imageType);
}