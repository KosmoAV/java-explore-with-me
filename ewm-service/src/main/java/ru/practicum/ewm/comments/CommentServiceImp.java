package ru.practicum.ewm.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.dto.NewCommentDto;
import ru.practicum.ewm.comments.dto.UpdateCommentDto;
import ru.practicum.ewm.comments.interfaces.CommentRepository;
import ru.practicum.ewm.comments.interfaces.CommentService;
import ru.practicum.ewm.comments.model.Comment;
import ru.practicum.ewm.comments.model.CommentSort;
import ru.practicum.ewm.events.interfaces.EventRepository;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.model.State;
import ru.practicum.ewm.exception.ConflictRequestException;
import ru.practicum.ewm.exception.DataNotFoundRequestException;
import ru.practicum.ewm.users.interfaces.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public CommentDto addComment(NewCommentDto newCommentDto, Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new DataNotFoundRequestException("User with id = " + userId + " was not found");
        }

        Event event = eventRepository.findById(newCommentDto.getEvent())
                .orElseThrow(() -> new DataNotFoundRequestException("Event with id = "
                        + newCommentDto.getEvent() + " not found"));

        if (event.getState() != State.PUBLISHED) {
            throw new ConflictRequestException("Event with id = " + event.getId() + " has not been published");
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictRequestException("User with id = " + event.getInitiator().getId() +
                    " is the initiator of the event");
        }

        Comment comment = CommentMapper.toComment(newCommentDto, userId);

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateComment(UpdateCommentDto updateCommentDto, Long userId, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundRequestException("Comment with id = " +
                        commentId + " not found"));

        if (!comment.getCommentator().equals(userId)) {
            throw new ConflictRequestException("User with id = " + userId + " is not the author of the comment");
        }

        comment.setText(updateCommentDto.getText());
        comment.setChangedOn(LocalDateTime.now());

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getCommentsByCommentator(Long userId) {

        List<Comment> comments = commentRepository.findByCommentator(userId);

        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {

        if (!commentRepository.existsByIdAndCommentator(commentId, userId)) {
            throw new DataNotFoundRequestException("Comment with id = " + commentId + " and commentator = "
            + userId + " not found");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getCommentsByEvent(Long eventId, CommentSort sort, Integer from, Integer size) {

        if (!eventRepository.existsByIdAndState(eventId, State.PUBLISHED)) {
            return new ArrayList<>();
        }

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        List<Comment> comments = null;

        switch (sort) {
            case ASCENDING_DATE:
                comments = commentRepository.findByEventOrderByCreatedOnAsc(eventId, page).getContent();
            break;
            case DESCENDING_DATE:
                comments = commentRepository.findByEventOrderByCreatedOnDesc(eventId, page).getContent();
            break;
        }

        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto updateCommentByAdmin(UpdateCommentDto updateCommentDto, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundRequestException("Comment with id = " +
                        commentId + " not found"));

        comment.setText(updateCommentDto.getText());
        comment.setChangedOn(LocalDateTime.now());

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {

        commentRepository.deleteById(commentId);
    }
}
