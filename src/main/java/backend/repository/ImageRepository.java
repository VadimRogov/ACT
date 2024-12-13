package backend.repository;

import backend.model.Image;
import backend.model.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByBookIdAndImageType(Long bookId, ImageType imageType);
}