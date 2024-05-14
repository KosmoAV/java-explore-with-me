package ru.practicum.ewm.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.dto.NewCommentDto;
import ru.practicum.ewm.comments.dto.UpdateCommentDto;
import ru.practicum.ewm.comments.interfaces.CommentRepository;
import ru.practicum.ewm.comments.interfaces.CommentService;
import ru.practicum.ewm.events.dto.EventShortDto;
import ru.practicum.ewm.events.dto.*;
import ru.practicum.ewm.events.interfaces.EventService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.interfaces.RequestService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UsersController {

    private final EventService eventService;
    private final RequestService requestService;
    private final CommentService commentService;

    @GetMapping(value = "/{userId}/events")
    public List<EventShortDto> getEvents(@PathVariable @Positive Long userId,
                                         @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {

        log.info("Call 'getEvents': {}, from = {}, size = {}", userId, from, size);

        return eventService.getEvents(userId, from, size);
    }

    @PostMapping(value = "/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@RequestBody @Validated NewEventDto newEventDto,
                                 @PathVariable @Positive Long userId) {

        log.info("Call 'addEvent': {}, userId = {}", newEventDto, userId);

        return eventService.addEvent(newEventDto, userId);
    }

    @GetMapping(value = "/{userId}/events/{eventId}")
    public EventFullDto getEvent(@PathVariable @Positive Long userId,
                                 @PathVariable @Positive Long eventId) {

        log.info("Call 'getEvent': userId = {}, eventId = {}", userId, eventId);

        return eventService.getEvent(eventId, userId);
    }

    @PatchMapping(value = "/{userId}/events/{eventId}")
    public EventFullDto updateEventByUser(@RequestBody @Validated UpdateEventUserRequest updateEventUserRequest,
                                          @PathVariable @Positive Long userId,
                                          @PathVariable @Positive Long eventId) {

        log.info("Call 'updateEventByUser': {}, userId = {}, eventId = {}", updateEventUserRequest, userId, eventId);

        return eventService.updateEventByUser(updateEventUserRequest, eventId, userId);
    }

    @GetMapping(value = "/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByOwner(@PathVariable @Positive Long userId,
                                                            @PathVariable @Positive Long eventId) {

        log.info("Call 'getRequestsByOwner': userId = {}, eventId = {}", userId, eventId);

        return requestService.getRequests(eventId, userId);
    }

    @PatchMapping(value = "/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestByOwner(
            @RequestBody @Validated EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId) {

        log.info("Call 'updateRequestByOwner': {}, userId = {}, eventId = {}",
                                                eventRequestStatusUpdateRequest, userId, eventId);

        return requestService.updateRequestsByOwner(eventRequestStatusUpdateRequest, eventId, userId);
    }

    @GetMapping(value = "/{userId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable @Positive Long userId) {

        log.info("Call 'getRequests': userId = {}", userId);

        return requestService.getRequests(userId);
    }

    @PostMapping(value = "/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable @Positive Long userId,
                                              @RequestParam @Positive Long eventId) {

        log.info("Call 'addRequest': userId = {}, eventId = {}", userId, eventId);

        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping(value = "/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable @Positive Long userId,
                                                 @PathVariable @Positive Long requestId) {

        log.info("Call 'cancelRequest': userId = {}, requestId = {}", userId, requestId);

        return requestService.cancelRequest(userId, requestId);
    }

    @PostMapping(value = "/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@RequestBody @Validated NewCommentDto newCommentDto,
                                 @PathVariable @Positive Long userId) {

        log.info("Call 'addComment': {}, userId = {}", newCommentDto, userId);

        return commentService.addComment(newCommentDto, userId);
    }

    @GetMapping(value = "/{userId}/comments")
    public List<CommentDto> getComments(@PathVariable @Positive Long userId) {

        log.info("Call 'getComments': userId = {}", userId);

        return commentService.getCommentsByCommentator(userId);
    }

    @PatchMapping(value = "/{userId}/comments/{commentId}")
    public CommentDto updateComment(@RequestBody @Validated UpdateCommentDto updateCommentDto,
                                    @PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long commentId) {

        log.info("Call 'updateComment': {}, userId = {}, commentId = {}", updateCommentDto, userId, commentId);

        return commentService.updateComment(updateCommentDto, userId, commentId);
    }

    @DeleteMapping(value = "/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long commentId) {

        log.info("Call 'deleteComment': userId = {}, commentId = {}", userId, commentId);

        commentService.deleteComment(userId, commentId);
    }
}
