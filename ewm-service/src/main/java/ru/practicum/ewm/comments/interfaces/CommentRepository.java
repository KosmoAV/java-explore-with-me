package ru.practicum.ewm.comments.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.comments.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByCommentator(Long userId);

    @Query(value = "SELECT c FROM Comment AS c WHERE c.event = :eventId ORDER BY c.createdOn ASC")
    Page<Comment> findByEventOrderByCreatedOnAsc(Long eventId, PageRequest page);

    @Query(value = "SELECT c FROM Comment AS c WHERE c.event = :eventId ORDER BY c.createdOn DESC")
    Page<Comment> findByEventOrderByCreatedOnDesc(Long eventId, PageRequest page);

    Boolean existsByIdAndCommentator(Long commentId, Long UserId);
}
