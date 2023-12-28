package me.twometrue.eventmanager.controllers;

import jakarta.validation.Valid;
import me.twometrue.eventmanager.models.Event;
import me.twometrue.eventmanager.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String getEventById(@PathVariable Long id, Model model, @RequestParam Map<String, String> params) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);
        model.addAttribute("edit", params.get("edit"));
        return "event";
    }

    @PostMapping("/events/{id}")
    public String saveEventById(@ModelAttribute("eventForm") @Valid Event eventForm, Model model, RedirectAttributes redirectAttributes) {

        Event event = eventService.saveEvent(eventForm);
        model.addAttribute("event", event);

        redirectAttributes.addAttribute("id", eventForm.getId());
        return "redirect:/events/{id}";
    }

    @GetMapping("/events/new")
    public String newEvent(Model model){
        model.addAttribute("event", new Event());
        model.addAttribute("edit", true);
        return "event";
    }

    @PostMapping("/events/new")
    public String createEvent(@ModelAttribute("eventForm") @Valid Event eventForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            return "event";
        }
//        eventForm.setDescription(eventForm.getDescription().replace("\n", "<br>"));
        Event savedEvent = eventService.saveEvent(eventForm);
        redirectAttributes.addAttribute("id", savedEvent.getId());
        return "redirect:/events/{id}";
    }

}
