package backend.service;

import backend.model.Image;
import backend.model.ImageType;
import backend.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    @Transactional
    public Image createImage(Long bookId, ImageType imageType, MultipartFile file) {
        try {
            byte[] imageData = file.getBytes(); // Получаем данные изображения

            Image image = Image.builder()
                    .bookId(bookId)
                    .imageType(imageType)
                    .imageData(imageData) // Сохраняем данные изображения
                    .build();

            return imageRepository.save(image);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Transactional
    public Image updateImage(Long bookId, ImageType imageType, MultipartFile file) {
        try {
            byte[] imageData = file.getBytes(); // Получаем данные нового изображения

            // Найти существующее изображение
            Image existingImage = imageRepository.findByBookIdAndImageType(bookId, imageType)
                    .orElseThrow(() -> new RuntimeException("Image not found"));

            // Обновляем данные изображения
            existingImage.setImageData(imageData);

            return imageRepository.save(existingImage);

        } catch (IOException e) {
            throw new RuntimeException("Failed to update file", e);
        }
    }

    @Transactional
    public void deleteImage(Long bookId, ImageType imageType) {
        // Найти изображение и удалить его
        Image existingImage = imageRepository.findByBookIdAndImageType(bookId, imageType)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        imageRepository.delete(existingImage);
    }

    public byte[] getImageByBookIdAndType(Long bookId, ImageType imageType) {
        Image image = imageRepository.findByBookIdAndImageType(bookId, imageType)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        return image.getImageData(); // Возвращаем данные изображения
    }
}