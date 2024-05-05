package ru.practicum.ewm.request.interfaces;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;

import java.util.List;

public interface RequestService {

    public ParticipationRequestDto addRequest(Long userId, Long eventId);

    public List<ParticipationRequestDto> getRequests(Long userId);

    public ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    public List<ParticipationRequestDto> getRequests(Long eventId, Long userId);

    public EventRequestStatusUpdateResult updateRequestsByOwner(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                                Long eventId, Long userId);
}
