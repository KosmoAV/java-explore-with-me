package ru.practicum.ewm;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.ewm.dto.ResponseStatsDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatsServerTests {

    private final StatsClient  statsClient;

    @Test
    void sendStatsAndGetStatsTest() {

        statsClient.setServerUrl("http://localhost:9090");

        boolean sendStats = statsClient.sendStats("MyApp", "/env_2024", "192.0.168.58", LocalDateTime.now());
        assertTrue(sendStats);

        sendStats = statsClient.sendStats("YouApp", "/15GGG", "10.13.0.1", LocalDateTime.now());
        assertTrue(sendStats);

        sendStats = statsClient.sendStats("YouApp", "/15GGG", "10.13.0.1", LocalDateTime.now());
        assertTrue(sendStats);

        List<ResponseStatsDto> listDto = null;
        listDto = statsClient.getStats(LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1), null, null);

        System.out.println(listDto);

        assertNotNull(listDto);
        assertEquals(2, listDto.size());
        assertEquals("YouApp", listDto.get(0).getApp());
        assertEquals("/15GGG", listDto.get(0).getUri());
        assertEquals(2, listDto.get(0).getHits());
        assertEquals("MyApp", listDto.get(1).getApp());
        assertEquals("/env_2024", listDto.get(1).getUri());
        assertEquals(1, listDto.get(1).getHits());

        listDto = statsClient.getStats(LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1), List.of("/15GGG"), null);

        System.out.println(listDto);

        assertNotNull(listDto);
        assertEquals(1, listDto.size());
        assertEquals("YouApp", listDto.get(0).getApp());
        assertEquals("/15GGG", listDto.get(0).getUri());
        assertEquals(2, listDto.get(0).getHits());

        listDto = statsClient.getStats(LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1), List.of("/15GGG"), true);

        System.out.println(listDto);

        assertNotNull(listDto);
        assertEquals(1, listDto.size());
        assertEquals("YouApp", listDto.get(0).getApp());
        assertEquals("/15GGG", listDto.get(0).getUri());
        assertEquals(1, listDto.get(0).getHits());

        listDto = statsClient.getStats(LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), List.of("/15GGG"), true);

        System.out.println(listDto);

        assertNotNull(listDto);
        assertEquals(0, listDto.size());

        listDto = statsClient.getStats(LocalDateTime.now().minusDays(100),
                LocalDateTime.now().plusDays(100), List.of("None"), true);

        System.out.println(listDto);

        assertNotNull(listDto);
        assertEquals(0, listDto.size());
    }
}
