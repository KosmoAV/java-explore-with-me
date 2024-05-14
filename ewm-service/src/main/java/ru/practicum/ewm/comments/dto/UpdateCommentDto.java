package ru.practicum.ewm.comments.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateCommentDto {

    @NotBlank
    @Size(min = 10, max = 1000)
    private String text;
}
