package ru.practicum.ewm;

import ru.practicum.ewm.dto.RequestStatsDto;
import ru.practicum.ewm.exception.DataBadRequestException;
import ru.practicum.ewm.interfaces.ResponseStatsDto;
import ru.practicum.ewm.model.Stat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class StatsMapper {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Stat toStat(RequestStatsDto requestStatsDto) {

        if (requestStatsDto == null) {
            return null;
        }

        Stat stat = new Stat();

        stat.setApp(requestStatsDto.getApp());
        stat.setUri(requestStatsDto.getUri());
        stat.setIp(requestStatsDto.getIp());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        try {
            stat.setTimeStamp(LocalDateTime.parse(requestStatsDto.getTimestamp(), formatter));
        } catch (DateTimeParseException e) {
            throw new DataBadRequestException("Data format error", "Wrong time format:" + requestStatsDto.getTimestamp()
                    + ". Must be " + DATE_TIME_FORMAT);
        }

        return stat;
    }

    public static List<ResponseStatsDto> toResponseStatsDto(List<Stat> stats) {

        if (stats == null) {
            return null;
        }

        if (stats.isEmpty()) {
            return new ArrayList<>();
        }

        return null;
    }
}
