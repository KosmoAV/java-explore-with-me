package ru.practicum.ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.RequestStatsDto;
import ru.practicum.ewm.interfaces.ResponseStatsDto;
import ru.practicum.ewm.interfaces.StatsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class StatsController {

    private final StatsService statsService;

    @PostMapping(value = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addRequest(@Validated @RequestBody RequestStatsDto requestStatsDto, HttpServletRequest request) {

        log.info("Call 'addRequest': {}", requestStatsDto);

        statsService.addRequest(requestStatsDto);
    }

    @GetMapping(value = "/stats")
    public List<ResponseStatsDto> getStats(
            @Validated @RequestParam @DateTimeFormat(pattern = StatsMapper.DATE_TIME_FORMAT) LocalDateTime start,
            @Validated @RequestParam @DateTimeFormat(pattern = StatsMapper.DATE_TIME_FORMAT) LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique,
            HttpServletRequest request) {

        log.info("Call 'getStats': {}, {}, {}, {}", start, end, uris, unique);

        return statsService.getStats(start, end, uris, unique);
    }
}
