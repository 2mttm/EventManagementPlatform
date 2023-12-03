package me.twometrue.eventmanager.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByIsFinishedOrderByStartAsc(boolean isFinished);
    List<Event> findByIsFinished(boolean isFinished);
    List<Event> findAllByOrderByViewsDesc();
    List<Event> findAllByOrderBySubscribersDesc();
}
