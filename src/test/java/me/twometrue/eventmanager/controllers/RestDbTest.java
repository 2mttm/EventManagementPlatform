package me.twometrue.eventmanager.controllers;

import jakarta.transaction.Transactional;
import me.twometrue.eventmanager.models.Event;
import me.twometrue.eventmanager.models.EventRepository;
import me.twometrue.eventmanager.models.User;
import me.twometrue.eventmanager.services.EventService;
import me.twometrue.eventmanager.services.UserService;
import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Rollback
@AutoConfigureMockMvc
public class RestDbTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private BeanFactory context;
    @BeforeEach
    public void setup(){
        Event event1 = new Event(1L, "Event 1");
        Event event2 = new Event(2L, "Event 2");

        User user = userService.findUserById(1L);

        eventService.saveEvent(event1, user);
        eventService.saveEvent(event2, user);
    }
    @Test
    public void mockPerform() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/events/megaSearch"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}
