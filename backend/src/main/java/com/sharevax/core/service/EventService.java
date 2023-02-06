package com.sharevax.core.service;

import com.sharevax.core.model.Harbor;
import com.sharevax.core.model.dto.ReportEventDto;
import com.sharevax.core.model.event.*;
import com.sharevax.core.repository.EventRepository;
import com.sharevax.core.repository.HarborRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                harbor.setStatus(Harbor.HarborStatus.CLOSED);
                harborRepository.save(harbor);
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

    public Set<String> findBlockedPassages(List<Event> activeEvents) {
        List<String> passageNames = Arrays.stream(BlockedPassage.PassageOption.values()).map(Enum::toString).toList();
        return findBlocked(activeEvents, passageNames);
    }

    public Set<String> findBlockedChannels(List<Event> activeEvents) {
        List<String> channelNames = Arrays.stream(BlockedChannel.ChannelOption.values()).map(Enum::toString).toList();
        return findBlocked(activeEvents, channelNames);
    }

    public Set<String> findBlockedStraits(List<Event> activeEvents) {
        List<String> straitNames = Arrays.stream(BlockedStrait.StraitOption.values()).map(Enum::toString).toList();
        return findBlocked(activeEvents, straitNames);
    }

    private Set<String> findBlocked(List<Event> activeEvents, List<String> namesToSearchFor) {
        var passageEvents = activeEvents.stream().filter(e -> namesToSearchFor.contains(e.getSubject())).toList();
        Set<String> blockedPassages = new HashSet<>();

        for (Event event : passageEvents) {
            blockedPassages.add(event.getSubject());
        }

        return blockedPassages;
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

    public Event finishEvent(int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event with supplied id not found"));
        event.setEventStatus(Event.EventStatus.COMPLETED);

        if (event instanceof BlockedHarbor) {
            var harbor = ((BlockedHarbor) event).getHarbor();
            harbor.setStatus(Harbor.HarborStatus.CLOSED);
            harborRepository.save(harbor);
        }
        return eventRepository.save(event);
    }
}
