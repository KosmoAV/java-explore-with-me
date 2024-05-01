package ru.practicum.ewm.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class RequestStatsDto {

    @NotNull
    @NotBlank
    private String app;
    @NotNull
    @NotBlank
    private String uri;
    @NotNull
    @NotBlank
    private String ip;
    @NotNull
    @NotBlank
    private String timestamp;

    void setTimestamp(LocalDateTime timestamp, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        this.timestamp = timestamp.format(formatter);
    }
}
