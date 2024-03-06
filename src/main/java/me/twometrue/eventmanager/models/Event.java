package me.twometrue.eventmanager.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    private User author;
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
    private boolean approvedByAuthor;
    private boolean approvedByEditor;

    private double latitude;
    private double longitude;

    @ManyToMany(mappedBy="subscriptions", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<User> subscribers = new HashSet<>();

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    public Event(Long id, String title){
        this.id = id;
        this.title = title;
    }

    public Event(String title, String description, String category, String location, LocalDateTime start, LocalDateTime end, User author) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
        this.category = category;
        this.isFinished = !end.isAfter(LocalDateTime.now());
    }
    public void addUser(User user) {
        subscribers.add(user);
        user.getSubscriptions().add(this);
    }

    public void removeUser(User user) {
        subscribers.remove(user);
        user.getSubscriptions().remove(this);
    }
    public boolean updateFinished(){
        this.isFinished = this.end.isBefore(LocalDateTime.now());
        return this.isFinished;
    }
    public Event update(Event other){
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
