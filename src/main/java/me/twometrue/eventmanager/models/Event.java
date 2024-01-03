package me.twometrue.eventmanager.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "events")
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //add onetomany
    private Long authorId;
    private String title;
    @Column(length = 65555)
    private String description;
    private String location;
    private LocalDateTime start;
    private LocalDateTime end;
    private int views;
    private boolean isFinished;
    private String category;
    @Column(length = 65555)
    private String imageUrl;
    private String theme;

    private double latitude;
    private double longitude;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> users = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "event")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Comment> comments = new HashSet<>();

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
    public void addUser(User user) {
        users.add(user);
        user.getEvents().add(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.getEvents().remove(this);
    }
    public boolean updateFinished(){
        this.isFinished = this.end.isBefore(LocalDateTime.now());
        return this.isFinished;
    }
    public Event update(Event other){
        this.authorId = other.getAuthorId();
        this.title = other.getTitle();
        this.description = other.getDescription();
        this.location = other.getLocation();
        this.start = other.getStart();
        this.end = other.getEnd();
        this.category = other.getCategory();
        this.imageUrl = other.getImageUrl();
        this.theme = other.getTheme();
        this.latitude = other.getLatitude();
        this.longitude = other.getLongitude();

        updateFinished();

        return this;
    }

}
