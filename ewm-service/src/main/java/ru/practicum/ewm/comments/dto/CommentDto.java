package ru.practicum.ewm.comments.dto;

import lombok.Data;

@Data
public class CommentDto {

    private Long id;

    private Long event;

    private Long commentator;

    private String text;

    private String createdOn;

    private String changedOn;
}
