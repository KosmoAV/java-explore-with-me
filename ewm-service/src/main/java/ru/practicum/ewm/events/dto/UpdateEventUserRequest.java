package ru.practicum.ewm.events.dto;

import ru.practicum.ewm.events.model.UserStateAction;

public class UpdateEventUserRequest extends BaseUpdateEventRequest {

    private UserStateAction stateAction;

    public UserStateAction getStateAction() {

        return stateAction;
    }
}
