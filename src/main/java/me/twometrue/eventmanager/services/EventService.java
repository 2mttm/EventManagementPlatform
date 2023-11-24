package me.twometrue.eventmanager.services;

import me.twometrue.eventmanager.models.Event;
import me.twometrue.eventmanager.models.EventRepository;
import me.twometrue.eventmanager.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event saveEvent(Event event) {
        if (event.getId() == null) {
            return eventRepository.save(event);
        }
        Event eventFromDB = eventRepository.findById(event.getId()).orElse(null);

        if (eventFromDB != null) {
            return eventFromDB;
        }
        eventRepository.save(event);
        return event;
    }

    public List<Event> getAllEventsSortedByStartTime() {
        return eventRepository.findAllByOrderByStartAsc();
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
}