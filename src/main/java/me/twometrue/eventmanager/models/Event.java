package me.twometrue.eventmanager.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "events")
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long authorId;
    private String title;
    private String description;
    private String location;
    private LocalDateTime start;
    private LocalDateTime end;
    private int views;
    private int subscribers;
    private boolean isFinished;
    private String category;
    private String imageUrl;
    private String theme;

    //add google maps

    public Event(String title, String description, String category, String location, LocalDateTime start, LocalDateTime end, Long authorId) {
        this.authorId = authorId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
        this.category = category;
        this.isFinished = !end.isAfter(LocalDateTime.now());
    }

}
