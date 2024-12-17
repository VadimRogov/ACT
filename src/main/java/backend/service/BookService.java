package backend.service;

import backend.model.Book;
import backend.model.ImageType;
import backend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    @Autowired
    private final ImageService imageService;

    public BookService(BookRepository bookRepository, ImageService imageService) {
        this.bookRepository = bookRepository;
        this.imageService = imageService;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public Book addBook(Book book) {
        // Сохраняем книгу
        Book savedBook = bookRepository.save(book);
        // Сохраняем обновленную книгу
        return bookRepository.save(savedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        // Найти книгу по ID
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Удалить связанные изображения
        if (book.getCover() != null) {
            imageService.deleteImage(book, ImageType.COVER);
        }

        if (book.getBookImage() != null) {
            imageService.deleteImage(book, ImageType.BOOK_IMAGE);
        }

        // Удалить книгу
        bookRepository.delete(book);
    }
}