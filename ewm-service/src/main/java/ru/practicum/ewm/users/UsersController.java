package ru.practicum.ewm.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.events.dto.EventShortDto;
import ru.practicum.ewm.events.dto.*;
import ru.practicum.ewm.users.dto.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UsersController {

    @GetMapping(value = "/{usersId}/events")
    public List<EventShortDto> getEvents(@PathVariable @Positive Long usersId,
                                         @Min(0) Long from,
                                         @Positive Long size) {

        log.info("Call 'getEvents': {}, {}, {}", usersId, from, size);

        return null;
    }

    @PostMapping(value = "/{usersId}/events")
    public EventFullDto addEvent(@RequestBody @Validated NewEventDto newEventDto,
                                 @PathVariable @Positive Long usersId) {

        log.info("Call 'getEvents': {}, {}", newEventDto, usersId);

        return null;
    }

    @GetMapping(value = "/{usersId}/events/{eventId}")
    public EventFullDto getEventById(@PathVariable @Positive Long usersId,
                                     @PathVariable @Positive Long eventId) {

        log.info("Call 'getEventById': {}, {}, {}", usersId, eventId);

        return null;
    }

    @PatchMapping(value = "/{usersId}/events/{eventId}")
    public EventFullDto updateEventById(@RequestBody @Validated UpdateEventUserRequest updateEventUserRequest,
                                        @PathVariable @Positive Long usersId,
                                        @PathVariable @Positive Long eventId) {

        log.info("Call 'updateEventById': {}, {}, {}", updateEventUserRequest, usersId, eventId);

        return null;
    }

    @GetMapping(value = "/{usersId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByOwner(@PathVariable @Positive Long usersId,
                                                     @PathVariable @Positive Long eventId) {

        log.info("Call 'getRequestsByOwner': {}, {}", usersId, eventId);

        return null;
    }

    @PatchMapping(value = "/users/{usersId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestByOwner(
            @RequestBody @Validated EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            @PathVariable @Positive Long usersId,
            @PathVariable @Positive Long eventId) {

        log.info("Call 'updateRequestByOwner': {}, {}, {}", eventRequestStatusUpdateRequest, usersId, eventId);

        return null;
    }

    @GetMapping(value = "/{usersId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable @Positive Long usersId) {

        log.info("Call 'getRequests': {}", usersId);

        return null;
    }

    @PostMapping(value = "/{usersId}/requests")
    public ParticipationRequestDto addRequest(@PathVariable @Positive Long usersId,
                                              @RequestParam @Positive Long eventId) {

        log.info("Call 'addRequest': {}, {}", usersId, eventId);

        return null;
    }

    @PatchMapping(value = "/{usersId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable @Positive Long usersId,
                                                 @PathVariable @Positive Long requestId) {

        log.info("Call 'cancelRequest': {}, {}", usersId, requestId);

        return null;
    }
}
