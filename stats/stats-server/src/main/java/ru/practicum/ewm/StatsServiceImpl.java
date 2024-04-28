package ru.practicum.ewm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.RequestStatsDto;
import ru.practicum.ewm.exception.DataBadRequestException;
import ru.practicum.ewm.interfaces.ResponseStatsDto;
import ru.practicum.ewm.interfaces.StatsRepository;
import ru.practicum.ewm.interfaces.StatsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public void addRequest(RequestStatsDto requestStatsDto) {

        statsRepository.save(StatsMapper.toStat(requestStatsDto));
    }

    @Override
    public List<ResponseStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        if (!start.isBefore(end)) {
            throw new DataBadRequestException("Data error", "Start time must be before End time");
        }

        List<ResponseStatsDto> responseStatsDtos = null;

        if (uris == null || uris.isEmpty()) {
            if (!unique) {
                responseStatsDtos = statsRepository.findByDateTime(start, end);
            }
            else {
                responseStatsDtos = statsRepository.findByDateTimeWithUniqueIp(start, end);
            }
        } else {
            if (!unique) {
                responseStatsDtos = statsRepository.findByDateTimeAndUris(start, end, uris);
            } else {
                responseStatsDtos = statsRepository.findByDateTimeAndUrisWithUniqueIp(start, end, uris);
            }
        }

        if (responseStatsDtos == null) {
            return new ArrayList<>();
        }

        return responseStatsDtos;
    }
}