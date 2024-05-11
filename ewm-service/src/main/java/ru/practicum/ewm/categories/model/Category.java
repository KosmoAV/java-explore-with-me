package ru.practicum.ewm.categories.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "categories", schema = "public")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;
}
