package ru.practicum.ewm.request;

import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.Request;

import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {

        ParticipationRequestDto participation = new ParticipationRequestDto();
        participation.setId(request.getId());
        participation.setEvent(request.getEvent());
        participation.setRequester(request.getRequester());
        participation.setCreated(request.getCreated());
        participation.setStatus(request.getStatus());

        return participation;
    }

    public static EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(List<Request> confirmedRequests,
                                                                                  List<Request> rejectedRequests) {

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();

        result.setConfirmedRequests(confirmedRequests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList()));

        result.setRejectedRequests(rejectedRequests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList()));

        return result;
    }
}
