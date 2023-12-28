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
                savedEvent.get().setTitle(event.getTitle());
                savedEvent.get().setDescription(event.getDescription());
                savedEvent.get().setLocation(event.getLocation());
                savedEvent.get().setImageUrl(event.getImageUrl());
                savedEvent.get().setStart(event.getStart());
                savedEvent.get().setEnd(event.getEnd());
                savedEvent.get().setLatitude(event.getLatitude());
                savedEvent.get().setLongitude(event.getLongitude());
                return eventRepository.save(savedEvent.get());
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
        return eventRepository.findAllByOrderBySubscribersDesc();
    }

    public Event getEventById(Long id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event != null) {
            addViews(event, 1);
        }
        return event;
    }


    private int addViews(Event event, int inc) {
        int newViews = event.getViews() + inc;
        event.setViews(newViews);
        eventRepository.save(event);
        return newViews;
    }

    @Scheduled(fixedDelay = 10000)
    private void checkEventsStatuses() {
        for (Event event : eventRepository.findByIsFinished(false)){
            if (event.getEnd().isBefore(LocalDateTime.now())){
                event.setFinished(true);
                eventRepository.save(event);
            }
        }
    }
}