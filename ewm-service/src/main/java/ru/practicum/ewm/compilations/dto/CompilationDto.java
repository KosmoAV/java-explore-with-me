package ru.practicum.ewm.compilations.dto;

import lombok.Data;
import ru.practicum.ewm.events.dto.EventShortDto;

import java.util.List;

@Data
public class CompilationDto {

    private Long id;

    private Boolean pinned;

    private String title;

    private List<EventShortDto> events;
}
