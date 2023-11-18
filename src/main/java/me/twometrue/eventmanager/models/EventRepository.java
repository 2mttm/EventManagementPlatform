package me.twometrue.eventmanager.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByOrderByStartAsc();
    List<Event> findAllByOrderByViewsDesc();
    List<Event> findAllByOrderBySubscribersDesc();
}
