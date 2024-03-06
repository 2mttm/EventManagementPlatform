package me.twometrue.eventmanager.controllers;

import me.twometrue.eventmanager.models.Event;
import me.twometrue.eventmanager.models.User;
import me.twometrue.eventmanager.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/events")
public class RestController {
    @Autowired
    private EventService eventService;

    @GetMapping("")
    public ResponseEntity<Page<Event>> getAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Event> events = eventService.findAll(pageable);
        return new ResponseEntity<>(events, events.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @GetMapping("/event")
    public ResponseEntity<Optional<Event>> getById(@RequestParam Long id) {
        Optional<Event> event = eventService.findById(id);
        return new ResponseEntity<>(event, event.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @GetMapping(value = "/megaSearch")
    public ResponseEntity<Page<Event>> getFilteredAndSorted(
            @ModelAttribute Event event,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable){

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                .withIgnorePaths("views", "isFinished", "latitude", "longitude", "approvedByAuthor", "approvedByEditor")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();

//        if (event.getStart() != null) {
//            exampleMatcher = exampleMatcher.withMatcher("start", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.DEFAULT)
//                    .ignoreCase()
//                    .transform(value -> {
//                        if (value.get() instanceof LocalDateTime) {
//                            return Optional.of(((LocalDateTime) value.get()).truncatedTo(ChronoUnit.HOURS));
//                        }
//                        return value;
//                    }));
//        }

        Example<Event> example = Example.of(event, exampleMatcher);

        Page<Event> events = eventService.findAll(example, pageable);
        return new ResponseEntity<>(events, events.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}
