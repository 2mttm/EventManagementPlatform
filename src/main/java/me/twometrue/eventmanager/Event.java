package me.twometrue.eventmanager;

import java.time.LocalDateTime;

public class Event {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;

    public Event(Long id, String title, String description, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
    }

    public Event() {
    }
}
