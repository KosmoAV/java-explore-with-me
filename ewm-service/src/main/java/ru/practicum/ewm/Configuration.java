package ru.practicum.ewm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Configuration {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    public Configuration(StatsClient statsClient, @Value("${stats-server.url}") String serverUrl) {

        statsClient.setServerUrl(serverUrl);
    }
}
