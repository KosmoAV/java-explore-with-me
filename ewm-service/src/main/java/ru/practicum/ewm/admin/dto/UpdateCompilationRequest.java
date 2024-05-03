package ru.practicum.ewm.admin.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UpdateCompilationRequest {

    private Set<Long> events;

    private Boolean pinned;

    @NotBlank
    @Size(min = 2, max = 250)
    private String title;
}
