package ru.practicum.ewm.events.dto;

import ru.practicum.ewm.events.model.AdminStateAction;

public class UpdateEventAdminRequest extends BaseUpdateEventRequest {

    private AdminStateAction stateAction;

    public AdminStateAction getStateAction() {

        return stateAction;
    }
}
