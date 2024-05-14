package ru.practicum.ewm.comments.interfaces;

import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.dto.NewCommentDto;
import ru.practicum.ewm.comments.dto.UpdateCommentDto;
import ru.practicum.ewm.comments.model.CommentSort;

import java.util.List;

public interface CommentService {

    public CommentDto addComment(NewCommentDto newCommentDto, Long userId);

    public CommentDto updateComment(UpdateCommentDto updateCommentDto, Long userId, Long commentId);

    public List<CommentDto> getCommentsByCommentator(Long userId);

    public List<CommentDto> getCommentsByEvent(Long eventId, CommentSort sort, Integer from, Integer size);

    public void deleteComment(Long userId, Long commentId);

    public CommentDto updateCommentByAdmin(UpdateCommentDto updateCommentDto, Long commentId);

    public void deleteCommentByAdmin(Long commentId);
}
