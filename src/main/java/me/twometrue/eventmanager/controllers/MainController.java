package me.twometrue.eventmanager.controllers;

import jakarta.validation.Valid;
import me.twometrue.eventmanager.models.Event;
import me.twometrue.eventmanager.models.User;
import me.twometrue.eventmanager.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;


@Controller
public class MainController {
    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public String home(Model model) {
        List<Event> events = eventService.getAllEventsSortedByStartTime();
        model.addAttribute("events", events);
        return "home";
    }

    @GetMapping("/popular")
    public String popular(Model model) {
        List<Event> events = eventService.getAllEventsSortedByViews();
        model.addAttribute("events", events);
        return "home";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/events/{id}")
    public String getEventById(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);

        return "event";
    }

    @GetMapping("/events/new")
    public String newEvent(Model model){
        model.addAttribute("eventForm", new Event());
        return "event";
    }

    @PostMapping("/events/new")
    public String createEvent(@ModelAttribute("eventForm") @Valid Event eventForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            return "event";
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Event savedEvent = eventService.saveEvent(eventForm);
        redirectAttributes.addAttribute("id", savedEvent.getId());
        return "redirect:/events/{id}";
    }

}
