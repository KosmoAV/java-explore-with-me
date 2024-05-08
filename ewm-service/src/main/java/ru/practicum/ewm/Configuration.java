package ru.practicum.ewm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Configuration {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static StatsClient statsClient;

    @Autowired
    public Configuration(StatsClient statsClient, @Value("${stats-server.url}") String serverUrl) {

        Configuration.statsClient = statsClient;
        Configuration.statsClient.setServerUrl(serverUrl);
    }
}
