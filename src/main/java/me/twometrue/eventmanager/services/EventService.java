package me.twometrue.eventmanager.services;

import me.twometrue.eventmanager.models.Event;
import me.twometrue.eventmanager.models.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@EnableScheduling
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event saveEvent(Event event) {
        if (event.getStart() == null) event.setStart(LocalDateTime.now());
        if (event.getEnd() == null) event.setEnd(LocalDateTime.now());

        event.setFinished(!event.getEnd().isAfter(LocalDateTime.now()));

        if (event.getId() != null) {
            Optional<Event> savedEvent = eventRepository.findById(event.getId());
            if (savedEvent.isPresent()) {
                return eventRepository.save(savedEvent.get().update(event));
            }
        }

        return eventRepository.save(event);
    }

    public List<Event> getUpcoming() {
        return eventRepository.findByIsFinishedOrderByStartAsc(false);
    }

    public List<Event> getAllEventsSortedByViews() {
        return eventRepository.findAllByOrderByViewsDesc();
    }

    public List<Event> getAllEventsSortedBySubscribers() {
        return eventRepository.findAllSortedByUsersCount();
    }

    public Event findEventById(Long id, int addViews) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event != null) {
            addViews(event, addViews);
            eventRepository.save(event);
        }
        return event;
    }


    protected int addViews(Event event, int inc) {
        int newViews = event.getViews() + inc;
        event.setViews(newViews);
        return newViews;
    }
    private void finishEvent(Event event){
        event.setFinished(true);
//        event.setUsers(null);
        eventRepository.save(event);
    }
    @Scheduled(fixedDelay = 30000)
    private void updateFinishedEvents() {
        for (Event event : eventRepository.findByIsFinished(false)) {
            if (event.getEnd().isBefore(LocalDateTime.now())) {
                finishEvent(event);
            }
        }
    }

}