package ru.practicum.ewm.interfaces;

import ru.practicum.ewm.dto.RequestStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    public void addRequest(RequestStatsDto requestStatsDto);

    public List<ResponseStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
