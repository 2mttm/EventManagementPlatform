package me.twometrue.eventmanager.controllers;

import me.twometrue.eventmanager.models.Role;
import me.twometrue.eventmanager.models.RoleRepository;
import me.twometrue.eventmanager.models.User;
import me.twometrue.eventmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        return "admin";
    }

    @PostMapping("/admin")
    public String  updateUser(@RequestParam Long userId,
                              @RequestParam String action,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String username,
                              @RequestParam(required = false) String rolesString,
                              Model model) {
        if (action.equals("delete")){
            userService.deleteUser(userId);
        } else if (action.equals("edit")) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : rolesString.split(" ")){
                Role role = userService.findByRoleName(roleName);
                if (role != null){
                    roles.add(role);
                }
            }
            //TODO: change to saveUser()
            userService.editUser(userId, new User(name, username, roles));
        }
        return "redirect:/admin";
    }

}