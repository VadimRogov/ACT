package backend.service;

import backend.model.Comment;
import backend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    // Добавить комментарий
    public Comment addComment(String content, String author) {
        Comment comment = Comment.builder()
                .content(content)
                .author(author)
                .build();
        return commentRepository.save(comment);
    }

    // Обновить комментарий
    public Comment updateComment(Long id, String content, String author) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setContent(content);
        comment.setAuthor(author);
        return commentRepository.save(comment);
    }

    // Удалить комментарий
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    // Получить все комментарии
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}