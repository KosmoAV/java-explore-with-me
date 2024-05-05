package ru.practicum.ewm.events.dto;

import lombok.Data;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.users.dto.UserShortDto;

@Data
public class EventShortDto {

    private Long id;

    private String title;

    private String annotation;

    private String eventDate;

    private Boolean paid;

    private Long views;

    private CategoryDto category;

    private UserShortDto initiator;

    private Long confirmedRequests = 0L;
}
