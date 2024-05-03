package ru.practicum.ewm.users.dto;


import lombok.Data;
import ru.practicum.ewm.users.model.Status;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private Status status;
}
