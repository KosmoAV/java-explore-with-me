package ru.practicum.ewm.users.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewUserRequest {

    @NotNull
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;

    @NotNull
    @Email
    @Size(min = 6, max = 254)
    private String email;
}
