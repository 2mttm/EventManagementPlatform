package me.twometrue.eventmanager.controllers;

import me.twometrue.eventmanager.configuration.MvcConfig;
import me.twometrue.eventmanager.configuration.WebSecurityConfig;
import me.twometrue.eventmanager.services.EventService;
import me.twometrue.eventmanager.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MainControllerTest {
    @Autowired
    EventService eventService;
    @Autowired
    UserService userService;
    @Autowired
    MainController mainController;
    @Autowired
    ControllerAdvice controllerAdvice;

    @Autowired
    WebApplicationContext wac;

    WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:8081").build();
    }
    @Test
    void getMainPage(){
        client.get()
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    void getExistingEvent(){
        client.get().uri("/events/6")
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    void getNotExistingEvent(){
        client.get().uri("/events/999")
                .exchange()
                .expectStatus().is3xxRedirection();
    }
    @Test
    void getLoginPage(){
        client.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }
}