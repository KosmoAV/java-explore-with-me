package ru.practicum.ewm.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.StatsClient;
import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.interfaces.CommentService;
import ru.practicum.ewm.comments.model.CommentSort;
import ru.practicum.ewm.events.dto.EventFullDto;
import ru.practicum.ewm.events.dto.EventShortDto;
import ru.practicum.ewm.events.interfaces.EventService;
import ru.practicum.ewm.events.model.Sort;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
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

    private final EventService eventService;
    private final CommentService commentService;
    private final StatsClient statsClient;

    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) Sort sort,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            HttpServletRequest request) {

        log.info("Call 'getEvents': text = {}, categories = {}, paid = {}, rangeStart = {}, rangeEnd = {}," +
                        "onlyAvailable = {}, sort = {}, from = {}, size = {}",
                        text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        statsClient.sendStats("ewm", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        return eventService.getEventsByFilter(text, categories, paid, rangeStart, rangeEnd,
                                              onlyAvailable, sort, from, size);
    }

    @GetMapping(value = "/{id}")
    public EventFullDto getEvent(@PathVariable @Positive Long id, HttpServletRequest request) {

        log.info("Call 'getEvent': id = {}", id);

        statsClient.sendStats("ewm", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        return eventService.getEvent(id, request.getRemoteAddr());
    }

    @GetMapping(value = "/{id}/comments")
    public List<CommentDto> getComments(@PathVariable @Positive Long id,
                                        @RequestParam(defaultValue = "ASCENDING_DATE") CommentSort sort,
                                        @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {

        log.info("Call 'getComments': id = {}, sort = {}, from = {}, size = {}", id, sort, from, size);

        return commentService.getCommentsByEvent(id, sort, from, size);
    }
}
