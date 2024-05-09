package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.events.interfaces.EventRepository;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.model.State;
import ru.practicum.ewm.exception.ConflictRequestException;
import ru.practicum.ewm.exception.DataNotFoundRequestException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.interfaces.RequestRepository;
import ru.practicum.ewm.request.interfaces.RequestService;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImp implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundRequestException("Event with id = " + eventId + " not found"));

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictRequestException("User with id = " + userId + " is the initiator of the event");
        }

        if (event.getState() != State.PUBLISHED) {
            throw new ConflictRequestException("Event with id = " + eventId + " has not been published");
        }

        Long confirmedRequests = requestRepository.getCountRequest(eventId, Status.CONFIRMED).orElse(0L);

        if (event.getParticipantLimit() != 0 && confirmedRequests.intValue() >= event.getParticipantLimit()) {
            throw new ConflictRequestException("Event with id = " + eventId + " has reached the maximum number of participants");
        }

        Request request = new Request();
        request.setEvent(eventId);
        request.setRequester(userId);
        request.setCreated(LocalDateTime.now());

        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            request.setStatus(Status.PENDING);
        } else {
            request.setStatus(Status.CONFIRMED);
        }

        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {

        return requestRepository.findByRequester(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new DataNotFoundRequestException("Request with id = " + requestId + "not found"));

        if (!userId.equals(request.getRequester())) {
            throw new ConflictRequestException("User with id = " + userId + " is not the originator of the request");
        }

        request.setStatus(Status.CANCELED);

        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long eventId, Long userId) {

        if(!eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
            throw new DataNotFoundRequestException("Event with id = " + eventId + " and Initiator with id = " +
                    userId + " not found");
        }

        return requestRepository.findByEvent(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateRequestsByOwner(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                                Long eventId, Long userId) {

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new DataNotFoundRequestException("Event with id = " + eventId +
                                                " and Initiator with id = " + userId + " not found"));

        int confirmedRequest = requestRepository.getCountRequest(eventId, Status.CONFIRMED)
                .orElse(0L).intValue();

        if (eventRequestStatusUpdateRequest.getStatus() == Status.CONFIRMED) {
            if (confirmedRequest >= event.getParticipantLimit()) {
                throw new ConflictRequestException("The limit of approved applications has already been reached");
            }
        }

        List<Request> requests = requestRepository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());

        if (requests.size() != eventRequestStatusUpdateRequest.getRequestIds().size()) {
            throw new DataNotFoundRequestException("Can not find all Requests");
        }

        if (event.getRequestModeration()) {

            Integer participantLimit = event.getParticipantLimit();

            if (eventRequestStatusUpdateRequest.getStatus() == Status.CONFIRMED) {

                if (participantLimit == 0) {
                    for (Request request : requests) {
                        if (request.getStatus() == Status.PENDING) {
                            request.setStatus(Status.CONFIRMED);
                        }
                    }
                } else {

                    for (Request request : requests) {
                        if (confirmedRequest < participantLimit && request.getStatus() == Status.PENDING) {
                            request.setStatus(Status.CONFIRMED);
                            confirmedRequest++;
                        } else if (request.getStatus() == Status.PENDING) {
                            request.setStatus(Status.REJECTED);
                        }
                    }
                }
            } else if (eventRequestStatusUpdateRequest.getStatus() == Status.REJECTED) {

                for (Request request : requests) {
                    if (request.getStatus() == Status.PENDING) {
                        request.setStatus(Status.REJECTED);
                    } else if (request.getStatus() == Status.CONFIRMED) {
                        throw new ConflictRequestException("Request with id = " + request.getId() +
                                " already has been confirmed");
                    }
                }
            }
        }

        requests = requestRepository.saveAll(requests);

        List<Request> confirmedRequests = requests.stream()
                .filter(request -> request.getStatus() == Status.CONFIRMED)
                .collect(Collectors.toList());

        List<Request> rejectedRequests = requests.stream()
                .filter(request -> request.getStatus() == Status.REJECTED)
                .collect(Collectors.toList());

        return RequestMapper.toEventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }
}
