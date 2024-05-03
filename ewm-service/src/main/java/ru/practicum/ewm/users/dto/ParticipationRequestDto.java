package ru.practicum.ewm.users.dto;

import lombok.Data;
import ru.practicum.ewm.events.model.State;

import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto {

    private Long id;

    private LocalDateTime created;

    private Long event;

    private Long requester;

    private State status;
}
