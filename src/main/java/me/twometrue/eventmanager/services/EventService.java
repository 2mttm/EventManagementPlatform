package me.twometrue.eventmanager.services;

import me.twometrue.eventmanager.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@EnableScheduling
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public EventService(EventRepository eventRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Event saveEvent(Event event, UserDetails userDetails) {
        if (event.getStart() == null) event.setStart(LocalDateTime.now());
        if (event.getEnd() == null) event.setEnd(LocalDateTime.now());
        if (event.getAuthor() == null && userDetails != null) event.setAuthor(userRepository.findByUsername(userDetails.getUsername()));

        event.setFinished(!event.getEnd().isAfter(LocalDateTime.now()));

        if (event.getId() != null) {
            Optional<Event> savedEvent = eventRepository.findById(event.getId());
            if (savedEvent.isPresent()) {
                if (savedEvent.get().getAuthor().getUsername().equals(userDetails.getUsername())) {
                    savedEvent.get().setApprovedByAuthor(true);
                    savedEvent.get().setApprovedByEditor(false);
                }
                if (userRepository.findByUsername(userDetails.getUsername()).hasRole(roleRepository.findByName("ROLE_EDITOR")) &&
                        !savedEvent.get().getAuthor().getUsername().equals(userDetails.getUsername())){
                    savedEvent.get().setApprovedByAuthor(false);
                    savedEvent.get().setApprovedByEditor(true);
                }
                return eventRepository.save(savedEvent.get().update(event));
            }
        } else {
            event.setApprovedByAuthor(true);
        }

        return eventRepository.save(event);
    }

    public Event approveEvent(Event event, UserDetails userDetails){
        if (event.getAuthor().getUsername().equals(userDetails.getUsername())){
            event.setApprovedByAuthor(true);
        }
        if (userRepository.findByUsername(userDetails.getUsername()).hasRole(roleRepository.findByName("ROLE_EDITOR")) &&
                !event.getAuthor().getUsername().equals(userDetails.getUsername())){
            event.setApprovedByEditor(true);
            System.out.println("Event " + event.getId() + " has been approved by " + userDetails.getUsername());
        }
        return eventRepository.save(event);
    }

    public List<Event> getUpcoming() {
        return eventRepository.findAllByIsFinishedAndApprovedByAuthorAndApprovedByEditorOrderByStartAsc(false, true, true);
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

    public List<Event> getUserEvents(User user){
        return eventRepository.findAllByAuthor(user);
    }

    public List<Event> getEventsToApprove(User user){
        return eventRepository.findAllByApprovedByEditorAndAuthorNot(false, user);
    }
    public int getNumberOfApprovableEvents(User user){
        return eventRepository.countByAuthorAndApprovedByAuthor(user, false);
    }
    public void toggleSubscription(Event event, User user){
        if (eventRepository.findById(event.getId()).isPresent()){
            if (event.getSubscribers().contains(user)) {
                event.removeUser(user);
            } else {
                event.addUser(user);
            }
            eventRepository.save(event);
        }
    }
    public void deleteEvent(Event event){
        event.getSubscribers().forEach(subscriber -> subscriber.getSubscriptions().remove(event));
        event.getComments().forEach(comment -> comment.setEvent(null));
        eventRepository.delete(event);
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