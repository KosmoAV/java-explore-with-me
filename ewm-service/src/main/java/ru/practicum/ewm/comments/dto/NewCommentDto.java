package ru.practicum.ewm.comments.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewCommentDto {

    @NotNull
    private Long event;

    @NotBlank
    @Size(min = 10, max = 1000)
    private String text;
}
