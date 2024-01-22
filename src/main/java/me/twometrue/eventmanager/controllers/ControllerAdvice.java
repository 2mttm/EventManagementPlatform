package me.twometrue.eventmanager.controllers;

import jakarta.servlet.http.HttpServletRequest;
import me.twometrue.eventmanager.services.EventService;
import me.twometrue.eventmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
    private final EventService eventService;
    private final UserService userService;
    @Autowired
    public ControllerAdvice(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @ExceptionHandler(Exception.class)
    public String handleDataTruncation(HttpServletRequest req, Exception ex, RedirectAttributes redirectAttributes) {
        System.out.println("Request: " + req.getRequestURL() + " raised " + ex);

        redirectAttributes.addFlashAttribute("error", "Holy guacamole! " + ex.getMessage());

        return "redirect:/";
    }

    @ModelAttribute("myEventsBadge")
    public int myEventsBadge(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null)
            return eventService.getNumberOfApprovableEvents(userService.findUserByEmail(userDetails.getUsername()));
        else return 0;
    }
}
