package me.twometrue.eventmanager.controllers;

import jakarta.validation.Valid;
import me.twometrue.eventmanager.models.Event;
import me.twometrue.eventmanager.models.User;
import me.twometrue.eventmanager.services.EventService;
import me.twometrue.eventmanager.services.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class MainController {
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        List<Event> events = eventService.getUpcoming();
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
    public String getEventById(@PathVariable Long id, Model model, @RequestParam Map<String, String> params, @AuthenticationPrincipal UserDetails userDetails) {
        Event event = eventService.getEventById(id, 1);
        User user = userService.findUserByEmail(userDetails.getUsername());
        model.addAttribute("event", event);
        model.addAttribute("edit", params.get("edit"));
        model.addAttribute("subscribed", event.getUsers().contains(user));
        System.out.println(model.getAttribute("subscribed"));
        return "event";
    }

    @PostMapping("/events/{id}")
    public String saveEventById(@ModelAttribute("eventForm") @Valid Event eventForm, Model model, RedirectAttributes redirectAttributes) {

        Event event = eventService.saveEvent(eventForm);
        model.addAttribute("event", event);

        redirectAttributes.addAttribute("id", eventForm.getId());
        return "redirect:/events/{id}";
    }

    @GetMapping("/events/{id}/toggleSubscription")
    public String toggleSubscription(@PathVariable Long id, Model model, @RequestParam Map<String, String> params, @AuthenticationPrincipal UserDetails userDetails){
        Event event = eventService.getEventById(id, 0);
        System.out.println(event.getUsers());
        User user = userService.findUserByEmail(userDetails.getUsername());
        if (event.getUsers().contains(user)){
            event.removeUser(user);
            System.out.println("User " + user.getUsername() + " unsubscribed from event " + event.getId());
        } else {
            event.addUser(user);
            System.out.println("User " + user.getUsername() + " subscribed to event " + event.getId());
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
    public String createEvent(@ModelAttribute("eventForm") @Valid Event eventForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            return "event";
        }
        Event savedEvent = eventService.saveEvent(eventForm);
        redirectAttributes.addAttribute("id", savedEvent.getId());
        return "redirect:/events/{id}";
    }

}
