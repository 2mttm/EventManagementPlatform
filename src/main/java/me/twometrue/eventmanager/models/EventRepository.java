package me.twometrue.eventmanager.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByIsFinishedOrderByStartAsc(boolean isFinished);
    List<Event> findByIsFinished(boolean isFinished);
    List<Event> findAllByOrderByViewsDesc();

    @Query("SELECT e FROM Event e LEFT JOIN e.users u GROUP BY e.id ORDER BY COUNT(u) DESC")
    List<Event> findAllSortedByUsersCount();
}
