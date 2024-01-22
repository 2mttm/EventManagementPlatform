package me.twometrue.eventmanager.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByIsFinishedOrderByStartAsc(boolean isFinished);
    List<Event> findByIsFinished(boolean isFinished);
    List<Event> findAllByOrderByViewsDesc();
    List<Event> findAllByAuthor(User user);
    List<Event> findAllByIsFinishedAndApprovedByAuthorAndApprovedByEditorOrderByStartAsc(boolean isFinished, boolean approvedByAuthor, boolean approvedByEditor);
    List<Event> findAllByApprovedByEditorAndAuthorNot(boolean approvedByEditor, User user);
    int countByAuthorAndApprovedByAuthor(User author, boolean approvedByAuthor);

    @Query("SELECT e FROM Event e LEFT JOIN e.subscribers u GROUP BY e.id ORDER BY COUNT(u) DESC")
    List<Event> findAllSortedByUsersCount();
}
