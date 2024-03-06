package me.twometrue.eventmanager.controllers;

import me.twometrue.eventmanager.configuration.WebSecurityConfig;
import me.twometrue.eventmanager.models.Event;
import me.twometrue.eventmanager.models.EventRepository;
import me.twometrue.eventmanager.models.User;
import me.twometrue.eventmanager.services.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(RestController.class)
@Import(WebSecurityConfig.class)
class RestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @InjectMocks
    private RestController restController;
    @Spy
    private List<String> spy = new ArrayList<>();
    @Test
    void spyTest(){
        spy.add("Hello");
        spy.add("World");

        verify(spy).add("Hello");
        verify(spy).add("World");

        assertEquals(2, spy.size());

        Mockito.when(spy.size()).thenReturn(100);
        assertEquals(100, spy.size());
    }
    @Test
    public void testGetAll() throws Exception {
        Event event1 = new Event(1L, "Event 1");
        Event event2 = new Event(2L, "Event 2");
        Page<Event> events = new PageImpl<>(List.of(event1, event2));

        when((eventService.findAll(any(Pageable.class)))).thenReturn(events);

        // Perform GET request
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Event 1"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].title").value("Event 2"));

    }

    @Test
    public void testGetById() throws Exception {
        // Mocking data
        Event event = new Event(1L, "Event 1");

        when(eventService.findById(1L)).thenReturn(java.util.Optional.of(event));

        // Perform GET request
        mockMvc.perform(get("/api/events/event?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Event 1"));
    }

    @Test
    public void testGetAllFiltered() throws Exception {
        // Mocking data
        Event event1 = new Event(1L, "Event1");
        Event event2 = new Event(2L, "Event2");
        Page<Event> events = new PageImpl<>(List.of(event1, event2));

        when(eventService.findAll(any(Example.class), any(Pageable.class))).thenReturn(events);

        // Perform GET request
        mockMvc.perform(get("/api/events/megaSearch"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Event1"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].title").value("Event2"));
    }
}