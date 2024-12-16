package backend.service;

import backend.model.Book;
import backend.model.Image;
import backend.model.ImageType;
import backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final ImageService imageService;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public Book addBook(Book book, MultipartFile coverFile, MultipartFile bookImageFile) {
        // Сохраняем книгу
        Book savedBook = bookRepository.save(book);

        // Сохраняем изображения и связываем их с книгой
        if (coverFile != null && !coverFile.isEmpty()) {
            Image cover = imageService.createImage(savedBook.getId(), ImageType.COVER, coverFile);
            savedBook.setCover(cover);
        }

        if (bookImageFile != null && !bookImageFile.isEmpty()) {
            Image bookImage = imageService.createImage(savedBook.getId(), ImageType.BOOK_IMAGE, bookImageFile);
            savedBook.setBookImage(bookImage);
        }

        // Сохраняем обновленную книгу
        return bookRepository.save(savedBook);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}