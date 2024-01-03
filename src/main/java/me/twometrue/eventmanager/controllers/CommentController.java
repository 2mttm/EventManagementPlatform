package me.twometrue.eventmanager.controllers;

import jakarta.validation.Valid;
import me.twometrue.eventmanager.models.Comment;
import me.twometrue.eventmanager.models.CommentRepository;
import me.twometrue.eventmanager.models.Event;
import me.twometrue.eventmanager.models.User;
import me.twometrue.eventmanager.services.CommentService;
import me.twometrue.eventmanager.services.EventService;
import me.twometrue.eventmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;

    @PostMapping("/events/{id}/comment")
    public String publishComment(@ModelAttribute("commentForm") @Valid Comment commentForm, @PathVariable Long id, Model model, @RequestParam Map<String, String> params, @AuthenticationPrincipal UserDetails userDetails){

        User user = userService.findUserByEmail(userDetails.getUsername());
        Event event = eventService.getEventById(commentForm.getEvent().getId(), 0);

        if (user == null || event == null) return "redirect:/events/{id}";

        commentForm.setUser(user);

        model.addAttribute("user", user);
        model.addAttribute("event", event);

        commentService.save(commentForm);
        return "redirect:/events/{id}";
    }
}
