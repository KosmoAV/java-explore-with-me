package ru.practicum.ewm.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stats", schema = "public")
@Data
public class Stat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app", nullable = false, length = 128)
    private String app;

    @Column(name = "uri", nullable = false, length = 128)
    private String uri;

    @Column(name = "ip", nullable = false, length = 16)
    private String ip;

    @Column(name = "time_stamp", nullable = false)
    private LocalDateTime timeStamp;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Stat)) {
            return false;
        }

        return id != null && id.equals(((Stat) o).getId());
    }

    @Override
    public int hashCode() {

        return id.intValue();
    }
}
