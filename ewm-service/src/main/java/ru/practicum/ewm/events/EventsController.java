package ru.practicum.ewm.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.events.dto.EventFullDto;
import ru.practicum.ewm.events.dto.EventShortDto;
import ru.practicum.ewm.events.model.Sort;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.Configuration.DATE_TIME_FORMAT;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
public class EventsController {

    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam @NotNull @NotBlank String text,
            @RequestParam @NotNull List<Long> categories,
            @RequestParam @NotNull Boolean paid,
            @RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam Sort sort,
            @RequestParam(defaultValue = "0") @Min(0)Long from,
            @RequestParam(defaultValue = "10") @Positive Long size) {

        log.info("Call 'getEvents': {}, {}, {}, {}, {}, {}, {}, {}, {}", text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        return null;
    }

    @GetMapping(value = "/{id}")
    public EventFullDto getEvent(@PathVariable @Positive Long id) {

        log.info("Call 'getEvent': {}", id);

        return null;
    }
}
