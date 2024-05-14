package ru.practicum.ewm.comments.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments", schema = "public")
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    Long event;

    @Column(name = "user_id", nullable = false)
    Long commentator;

    @Column(name = "text", nullable = false, length = 1000)
    private String text;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "changed_on")
    private LocalDateTime changedOn;
}
