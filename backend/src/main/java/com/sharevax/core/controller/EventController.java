package com.sharevax.core.controller;

import com.sharevax.core.model.dto.ReportEventDto;
import com.sharevax.core.model.event.Event;
import com.sharevax.core.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
    // TODO: Implement this controller

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    private ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @PostMapping
    private ResponseEntity<Event> saveEvent(@RequestBody ReportEventDto event) {
        return ResponseEntity.ok(eventService.saveEvent(event));
    }
}
