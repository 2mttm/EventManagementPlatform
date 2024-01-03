package me.twometrue.eventmanager.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String content;
    private LocalDateTime creationTime;
    public String toString(){
        return "Comment(id=" + this.id + ", event=" + this.event.getId() + ", author=" + this.user.getId() +
                ", creationTime=" + this.creationTime + ", content=" + this.content + ")";
    }
}
