package me.twometrue.eventmanager.controllers;

import jakarta.validation.Valid;
import me.twometrue.eventmanager.models.Event;
import me.twometrue.eventmanager.models.User;
import me.twometrue.eventmanager.services.EventService;
import me.twometrue.eventmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Event> events = eventService.getUpcoming();
        model.addAttribute("events", events);

        if (userDetails != null) {
            User currentUser = userService.findUserByEmail(userDetails.getUsername());
            model.addAttribute("currentUser", currentUser);
        }

        return "home";
    }

    @GetMapping("/profile/{id}")
    public String profile(Model model, @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        User user = userService.findUserById(id);
        model.addAttribute("user", user);

        if (userDetails != null) {
            User currentUser = userService.findUserByEmail(userDetails.getUsername());
            model.addAttribute("currentUser", currentUser);
        }

        return "profile";
    }

    @GetMapping("/popular")
    public String popular(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Event> events = eventService.getAllEventsSortedByViews();
        model.addAttribute("events", events);

        if (userDetails != null) {
            User currentUser = userService.findUserByEmail(userDetails.getUsername());
            model.addAttribute("currentUser", currentUser);
        }

        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/events/{id}")
    public String getEventById(@PathVariable Long id, Model model, @RequestParam Map<String, String> params, @AuthenticationPrincipal UserDetails userDetails) {
        Event event = eventService.findEventById(id, 1);

        model.addAttribute("event", event);
        model.addAttribute("edit", params.get("edit"));

        if (userDetails != null){
            User user = userService.findUserByEmail(userDetails.getUsername());
            model.addAttribute("user", user);
            model.addAttribute("subscribed", event.getSubscribers().contains(user));
        }

        return "event";
    }

    @PostMapping("/events/{id}")
    public String saveEventById(@ModelAttribute("eventForm") @Valid Event eventForm, Model model, RedirectAttributes redirectAttributes) {

        Event event = eventService.saveEvent(eventForm);
        model.addAttribute("event", event);

        redirectAttributes.addAttribute("id", event.getId());
        return "redirect:/events/{id}";
    }

    @GetMapping("/events/{id}/toggleSubscription")
    public String toggleSubscription(@PathVariable Long id, Model model, @RequestParam Map<String, String> params, @AuthenticationPrincipal UserDetails userDetails) {
        Event event = eventService.findEventById(id, 0);
        User user = userService.findUserByEmail(userDetails.getUsername());
        if (event.getSubscribers().contains(user)) {
            event.removeUser(user);
        } else {
            event.addUser(user);
        }
        eventService.saveEvent(event);
        return "redirect:/events/{id}";
    }

    @GetMapping("/events/new")
    public String newEvent(Model model) {

        model.addAttribute("event", new Event());
        model.addAttribute("edit", true);
        return "event";
    }

    @PostMapping("/events/new")
    public String createEvent(@ModelAttribute("eventForm") @Valid Event eventForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "event";
        }
        Event savedEvent = eventService.saveEvent(eventForm);
        redirectAttributes.addAttribute("id", savedEvent.getId());
        return "redirect:/events/{id}";
    }

}
