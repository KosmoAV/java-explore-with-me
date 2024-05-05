package ru.practicum.ewm.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewm.events.model.Location;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.Configuration.DATE_TIME_FORMAT;

@Data
public class NewEventDto {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @NotNull
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    @Min(0)
    private Integer participantLimit = 0;

    private Boolean paid = false;

    private Boolean requestModeration = true;

    @NotNull
    private Long category;
}
