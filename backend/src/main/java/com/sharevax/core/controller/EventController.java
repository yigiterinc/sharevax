package com.sharevax.core.controller;

import com.sharevax.core.model.dto.EventDto;
import com.sharevax.core.model.dto.ReportEventDto;
import com.sharevax.core.model.event.Event;
import com.sharevax.core.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    private ResponseEntity<List<EventDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents().stream().map(EventDto::fromEvent).toList());
    }

    @PostMapping
    private ResponseEntity<Event> saveEvent(@RequestBody ReportEventDto event) {
        return ResponseEntity.ok(eventService.saveEvent(event));
    }

    @PatchMapping("/finish/{id}")
    private ResponseEntity<Event> finishEvent(@PathVariable int id) {
        return ResponseEntity.ok(eventService.finishEvent(id));
    }
}
