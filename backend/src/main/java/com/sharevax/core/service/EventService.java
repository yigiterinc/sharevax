package com.sharevax.core.service;

import com.sharevax.core.model.Harbor;
import com.sharevax.core.model.dto.ReportEventDto;
import com.sharevax.core.model.event.*;
import com.sharevax.core.repository.EventRepository;
import com.sharevax.core.repository.HarborRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final HarborRepository harborRepository;

    public EventService(EventRepository eventRepository,
                        HarborRepository harborRepository) {

        this.eventRepository = eventRepository;
        this.harborRepository = harborRepository;
    }

    public Event saveEvent(ReportEventDto event) {
        Event delayingEvent = null;

        if (event.isBlockedChannel()) {
            delayingEvent = new BlockedChannel(event.getSubject(), event.getRemainingDaysToStart());
        } else if (event.isBlockedHarbor()) {
            Harbor harbor = null;
            try {
                harbor = harborRepository.findHarborByName(event.getSubject());
            } catch (Exception e) {
                throw new RuntimeException("Harbor with supplied country name not found while creating event");
            }
            delayingEvent = new BlockedHarbor(harbor, event.getRemainingDaysToStart());
        } else if (event.isBlockedPassage()) {
            delayingEvent = new BlockedPassage(event.getSubject(), event.getRemainingDaysToStart());
        } else if (event.isBlockedStrait()) {
            delayingEvent = new BlockedStrait(event.getSubject(), event.getRemainingDaysToStart());
        } else if (event.isBlockedChannel()) {
            delayingEvent = new BlockedChannel(event.getSubject(), event.getRemainingDaysToStart());
        }

        return eventRepository.save(delayingEvent);
    }

    public void processEvents() {
        List<Event> eventsToBeProcessed = eventRepository.findAllByEventStatus(Event.EventStatus.PENDING);

        for (Event event : eventsToBeProcessed) {
            event.setRemainingDaysToStart(event.getRemainingDaysToStart() - 1);
            if (event.getRemainingDaysToStart() <= 0) {
                event.startEvent();
                if (event instanceof BlockedHarbor) {
                    harborRepository.save(((BlockedHarbor) event).getHarbor());
                }
            }

            eventRepository.save(event);
        }
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getActiveEvents() {
        return eventRepository.findAllByEventStatus(Event.EventStatus.ACTIVE);
    }
}
