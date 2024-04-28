package ru.practicum.ewm;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.RequestStatsDto;
import ru.practicum.ewm.dto.ResponseStatsDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatsClient {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestTemplateBuilder restTemplateBuilder;
    private RestTemplate restTemplate;
    private String serverUrl;

    public void setServerUrl(String serverUrl) {

        restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();

        this.serverUrl = serverUrl;
    }

    public boolean sendStats(String app, String uri, String ip, LocalDateTime timeStamp) {

        RequestStatsDto body = new RequestStatsDto();
        body.setApp(app);
        body.setUri(uri);
        body.setIp(ip);

        body.setTimestamp(timeStamp.format(DATE_TIME_FORMATTER));

        HttpEntity<RequestStatsDto> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> serverResponse;

        try {
            serverResponse = restTemplate.exchange("/hit", HttpMethod.POST, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return false;
        }

        return serverResponse.getStatusCode().is2xxSuccessful();
    }

    public List<ResponseStatsDto> getStats(LocalDateTime start, LocalDateTime end,
                                           List<String> uris, Boolean unique) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.format(DATE_TIME_FORMATTER));
        parameters.put("end", end.format(DATE_TIME_FORMATTER));

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("/stats?start={start}&end={end}");

        if (uris != null) {
            StringBuilder urisStringBuilder = new StringBuilder();
            for (String uri : uris) {
                urisStringBuilder.append(uri);
                urisStringBuilder.append(",");
            }
            urisStringBuilder.deleteCharAt(urisStringBuilder.length() - 1);

            queryBuilder.append("&uris={uris}");
            parameters.put("uris", urisStringBuilder.toString());
        }

        if (unique != null) {
            queryBuilder.append("&unique={unique}");
            parameters.put("unique", unique);
        }

        HttpEntity<Object> requestEntity = new HttpEntity<>(defaultHeaders());

        ResponseEntity<ResponseStatsDto[]> serverResponse;

        try {
            serverResponse = restTemplate.exchange(queryBuilder.toString(),
                    HttpMethod.GET, requestEntity, ResponseStatsDto[].class, parameters);
        } catch (HttpStatusCodeException e) {
            return new ArrayList<>();
        }

        if (serverResponse.getBody() != null) {
            return Arrays.stream(serverResponse.getBody()).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setAcceptCharset(List.of(StandardCharsets.UTF_8));
        return headers;
    }
}
