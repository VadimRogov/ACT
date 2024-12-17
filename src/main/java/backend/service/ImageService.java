package backend.service;

import backend.model.Book;
import backend.model.Image;
import backend.model.ImageType;
import backend.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Transactional
    public Image createImage(Book book, ImageType imageType, MultipartFile file) {
        try {
            byte[] imageData = file.getBytes(); // Получаем данные изображения

            // Проверяем, существует ли уже изображение с таким типом для данной книги
            Optional<Image> existingImage = imageRepository.findByBookImageAndImageType(book, imageType);
            if (existingImage.isPresent()) {
                throw new RuntimeException("Image of type " + imageType + " already exists for this book.");
            }

            // Создаем новое изображение
            Image image = Image.builder()
                    .bookImage(book) // Устанавливаем связь с книгой
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
            Image existingImage = imageRepository.findByBookImageAndImageType(book, imageType)
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
        Image existingImage = imageRepository.findByBookImageAndImageType(book, imageType)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        imageRepository.delete(existingImage);
    }

    public byte[] getImageByBookAndType(Book book, ImageType imageType) {
        Image image = imageRepository.findByBookImageAndImageType(book, imageType)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        return image.getImageData(); // Возвращаем данные изображения
    }
}