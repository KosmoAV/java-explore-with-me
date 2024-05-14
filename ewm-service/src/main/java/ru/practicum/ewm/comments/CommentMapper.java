package ru.practicum.ewm.comments;

import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.dto.NewCommentDto;
import ru.practicum.ewm.comments.model.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.ewm.Configuration.DATE_TIME_FORMAT;

public class CommentMapper {

    public static Comment toComment(NewCommentDto newCommentDto, Long userId) {

        Comment comment = new Comment();
        comment.setEvent(newCommentDto.getEvent());
        comment.setCommentator(userId);
        comment.setText(newCommentDto.getText());
        comment.setCreatedOn(LocalDateTime.now());

        return comment;
    }

    public static CommentDto toCommentDto(Comment comment) {

        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setEvent(comment.getEvent());
        commentDto.setCommentator(comment.getCommentator());
        commentDto.setText(comment.getText());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        commentDto.setCreatedOn(comment.getCreatedOn().format(formatter));

        if (comment.getChangedOn() != null) {
            commentDto.setChangedOn(comment.getChangedOn().format(formatter));
        } else {
            commentDto.setChangedOn(null);
        }

        return commentDto;
    }
}
