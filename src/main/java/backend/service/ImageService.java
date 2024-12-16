package backend.service;

import backend.model.Book;
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
    public Image createImage(Book book, ImageType imageType, MultipartFile file) {
        try {
            byte[] imageData = file.getBytes(); // Получаем данные изображения

            Image image = Image.builder()
                    .book(book) // Устанавливаем связь с книгой
                    .imageType(imageType)
                    .imageData(imageData) // Сохраняем данные изображения
                    .build();

            return imageRepository.save(image);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Transactional
    public Image updateImage(Book book, ImageType imageType, MultipartFile file) {
        try {
            byte[] imageData = file.getBytes(); // Получаем данные нового изображения

            // Найти существующее изображение
            Image existingImage = imageRepository.findByBookAndImageType(book, imageType)
                    .orElseThrow(() -> new RuntimeException("Image not found"));

            // Обновляем данные изображения
            existingImage.setImageData(imageData);

            return imageRepository.save(existingImage);

        } catch (IOException e) {
            throw new RuntimeException("Failed to update file", e);
        }
    }

    @Transactional
    public void deleteImage(Book book, ImageType imageType) {
        // Найти изображение и удалить его
        Image existingImage = imageRepository.findByBookAndImageType(book, imageType)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        imageRepository.delete(existingImage);
    }

    public byte[] getImageByBookAndType(Book book, ImageType imageType) {
        Image image = imageRepository.findByBookAndImageType(book, imageType)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        return image.getImageData(); // Возвращаем данные изображения
    }
}