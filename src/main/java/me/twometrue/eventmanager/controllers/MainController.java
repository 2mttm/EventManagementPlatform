package me.twometrue.eventmanager.controllers;

import jakarta.servlet.http.HttpServletRequest;
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
    public String getEventById(@PathVariable Long id, Model model, @RequestParam Map<String, String> params,
                               @AuthenticationPrincipal UserDetails userDetails) {
        Event event = eventService.findEventById(id, 1);

        model.addAttribute("event", event);
        model.addAttribute("edit", params.get("edit"));

        if (userDetails != null){
            User currentUser = userService.findUserByEmail(userDetails.getUsername());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("subscribed", event.getSubscribers().contains(currentUser));
        }

        if (event != null) return "event";
        else return "redirect:/events";
    }

    @PostMapping("/events/{id}")
    public String saveEventById(@ModelAttribute("eventForm") @Valid Event eventForm, Model model,
                                RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal UserDetails userDetails) {

        Event event = eventService.saveEvent(eventForm, userDetails);
        model.addAttribute("event", event);

        redirectAttributes.addAttribute("id", event.getId());
        return "redirect:/events/{id}";
    }

    @GetMapping("/events/{id}/toggleSubscription")
    public String toggleSubscription(@PathVariable Long id, Model model, @RequestParam Map<String, String> params,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        Event event = eventService.findEventById(id, 0);
        User user = userService.findUserByEmail(userDetails.getUsername());

        eventService.toggleSubscription(event, user);
        return "redirect:/events/{id}";
    }

    @GetMapping("/events/new")
    public String newEvent(Model model) {

        model.addAttribute("event", new Event());
        model.addAttribute("edit", true);
        return "event";
    }

    @PostMapping("/events/new")
    public String createEvent(@ModelAttribute("eventForm") @Valid Event eventForm,
                              BindingResult bindingResult,
                              Model model, RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "event";
        }
        Event savedEvent = eventService.saveEvent(eventForm, userDetails);
        redirectAttributes.addAttribute("id", savedEvent.getId());
        return "redirect:/events/{id}";
    }

    @GetMapping("/my-events")
    public String myEvents(Model model, @AuthenticationPrincipal UserDetails userDetails){

        List<Event> events = eventService.getUserEvents(userService.findUserByEmail(userDetails.getUsername()));
        model.addAttribute("events", events);

        User currentUser = userService.findUserByEmail(userDetails.getUsername());
        model.addAttribute("currentUser", currentUser);

        return "home";
    }
    @GetMapping("/events/{id}/approve")
    public String approveEvent(@PathVariable Long id, Model model, @RequestParam Map<String, String> params,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        Event event = eventService.findEventById(id, 0);
        eventService.approveEvent(event, userDetails);
        return "redirect:/events/{id}";
    }

    @GetMapping("/approve-events")
    public String approveEvents(Model model, @AuthenticationPrincipal UserDetails userDetails){

        List<Event> events = eventService.getEventsToApprove(userService.findUserByEmail(userDetails.getUsername()));
        model.addAttribute("events", events);

        User currentUser = userService.findUserByEmail(userDetails.getUsername());
        model.addAttribute("currentUser", currentUser);

        return "home";
    }

    @GetMapping("/events/{id}/delete")
    public String deleteEvent(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request){
        Event event = eventService.findEventById(id, 0);
        if (request.isUserInRole("ROLE_EDITOR") || event.getAuthor().getUsername().equals(userDetails.getUsername())) {
            eventService.deleteEvent(event);
        }

        return "redirect:/";
    }

}
