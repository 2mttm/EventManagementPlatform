package me.twometrue.eventmanager.configuration;

import me.twometrue.eventmanager.models.Role;
import me.twometrue.eventmanager.models.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (roleRepository.findByName("ROLE_USER") == null) {
            Role userRole = new Role(1L, "ROLE_USER");
            roleRepository.save(userRole);
        }

        if (roleRepository.findByName("ROLE_EDITOR") == null) {
            Role adminRole = new Role(2L, "ROLE_EDITOR");
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            Role adminRole = new Role(3L, "ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

    }
}