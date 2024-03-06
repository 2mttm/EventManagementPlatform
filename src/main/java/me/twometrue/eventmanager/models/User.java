package me.twometrue.eventmanager.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    private String imageSrc;
    private String about;
    @Size(min = 3, message = "Minimum 3 symbols")
    private String name;
    @Column(name = "username", unique = true)
    private String username;
    @JsonIgnore
    private LocalDate birthday;
    @ToString.Exclude
    @JsonIgnore
    private String password;
    @Transient
    private int age;

    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<Event> subscriptions = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    public User(String name, String username, LocalDate birthday) {
        this.name = name;
        this.username = username;
        this.birthday = birthday;
    }

    public User(String name, String username, Set<Role> roles) {
        this.name = name;
        this.username = username;
        this.roles = roles;
    }

    public int getAge() {
        if (this.birthday == null) {
            return 0;
        }
        return Period.between(this.birthday, LocalDate.now()).getYears();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void addEvent(Event event) {
        subscriptions.add(event);
        event.getSubscribers().add(this);
    }

    public void removeEvent(Event event) {
        subscriptions.remove(event);
        event.getSubscribers().remove(this);
    }
    public boolean hasRole(Role role){
        return this.roles.contains(role);
    }
}
