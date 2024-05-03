package ru.practicum.ewm.users.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.events.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.Configuration.DATE_TIME_FORMAT;

@Data
public class NewEventDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid = false;

    private Long participantLimit = 0L;

    private Boolean requestModeration = true;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
