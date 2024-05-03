package ru.practicum.ewm.users.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserDto {

    private Long id;

    @Email
    private String email;

    private String name;
}
