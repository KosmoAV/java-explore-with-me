package ru.practicum.ewm.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stat, Long> {

    @Query(value = "SELECT s.app AS app, s.uri AS uri, COUNT(s.uri) AS hits FROM Stat s WHERE " +
            "s.timeStamp >= :startDateTime AND s.timeStamp <= :endDateTime GROUP BY s.app, s.uri ORDER BY hits DESC")
    public List<ResponseStatsDto> findByDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = "SELECT t.app, t.uri, COUNT(t.uri) AS hits FROM " +
            "(SELECT app, uri, ip, COUNT (uri) FROM stats " +
            "WHERE time_stamp >= :startDateTime AND time_stamp <= :endDateTime " +
            "GROUP BY app, uri, ip ORDER BY ip) AS t " +
            "GROUP BY t.app, t.uri ORDER BY hits DESC", nativeQuery = true)
    public List<ResponseStatsDto> findByDateTimeWithUniqueIp(LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = "SELECT s.app AS app, s.uri AS uri, COUNT(uri) AS hits FROM Stat s WHERE " +
            "s.timeStamp >= :startDateTime AND s.timeStamp <= :endDateTime AND s.uri IN (:uris) " +
            "GROUP BY s.app, s.uri ORDER BY hits DESC")
    public List<ResponseStatsDto> findByDateTimeAndUris(LocalDateTime startDateTime, LocalDateTime endDateTime, List<String> uris);

    @Query(value = "SELECT t.app, t.uri, COUNT(t.uri) AS hits FROM " +
            "(SELECT app, uri, ip, COUNT (uri) FROM stats " +
            "WHERE time_stamp >= :startDateTime AND time_stamp <= :endDateTime AND uri IN (:uris)" +
            "GROUP BY app, uri, ip ORDER BY ip) AS t " +
            "GROUP BY t.app, t.uri ORDER BY hits DESC", nativeQuery = true)
    public List<ResponseStatsDto> findByDateTimeAndUrisWithUniqueIp(LocalDateTime startDateTime, LocalDateTime endDateTime, List<String> uris);
}
