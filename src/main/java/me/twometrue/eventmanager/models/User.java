package me.twometrue.eventmanager.models;

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
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageSrc;
    private String about;
    @Size(min = 3, message = "Minimum 3 symbols")
    private String name;
    @Column(name = "username", unique = true)
    private String username;
    private LocalDate birthday;
    @ToString.Exclude
    private String password;
    @Transient
    private int age;

    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<Role> roles;

    @ManyToMany
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Event> subscriptions = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
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
}
