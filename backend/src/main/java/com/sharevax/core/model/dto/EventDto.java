package com.sharevax.core.model.dto;

import com.sharevax.core.model.event.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {

    enum SubjectType {
        HARBOR,
        STRAIT,
        PASSAGE,
        CHANNEL
    }

    int id;
    String subject;
    int remainingDaysToStart;
    String eventStatus;

    @Enumerated(EnumType.STRING)
    SubjectType type;

    public static EventDto fromEvent(Event event) {
        EventDto dto = new EventDto();
        dto.id = event.getId();
        dto.subject = event.getSubject();
        dto.remainingDaysToStart = event.getRemainingDaysToStart();
        dto.eventStatus = event.getEventStatus().toString();

        if (event instanceof BlockedHarbor) {
            dto.type = SubjectType.HARBOR;
        } else if (event instanceof BlockedStrait) {
            dto.type = SubjectType.STRAIT;
        } else if (event instanceof BlockedPassage) {
            dto.type = SubjectType.PASSAGE;
        } else if (event instanceof BlockedChannel) {
            dto.type = SubjectType.CHANNEL;
        }

        return dto;
    }
}

