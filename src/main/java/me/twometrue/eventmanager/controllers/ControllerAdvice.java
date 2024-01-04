package me.twometrue.eventmanager.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(Exception.class)
    public String handleDataTruncation(HttpServletRequest req, Exception ex, RedirectAttributes redirectAttributes) {
        System.out.println("Request: " + req.getRequestURL() + " raised " + ex);

        redirectAttributes.addFlashAttribute("error", "Holy guacamole! " + ex.getMessage());

        return "redirect:/";
    }
}
