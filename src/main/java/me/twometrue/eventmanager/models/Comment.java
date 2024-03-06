package me.twometrue.eventmanager.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    private Event event;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    private User user;
    private String content;
    private LocalDateTime creationTime;
    public String toString(){
        return "Comment(id=" + this.id + ", event=" + this.event.getId() + ", author=" + this.user.getId() +
                ", creationTime=" + this.creationTime + ", content=" + this.content + ")";
    }
}
