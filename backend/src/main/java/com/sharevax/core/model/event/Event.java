package com.sharevax.core.model.event;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delaying_event")
@DiscriminatorColumn(name = "event_type")
public abstract class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected Integer id;

    // Name of the affected harbor, strait, passage or channel
    @Column(name = "subject")
    protected String subject;

    public Event(String subject, int remainingDaysToStart) {
        this.subject = subject;
        this.remainingDaysToStart = remainingDaysToStart;

        if (remainingDaysToStart == 0) {
            this.eventStatus = EventStatus.ACTIVE;
        }
    }

    protected abstract String getDescriptiveMessage();

    /*
        * This method is called when the event is triggered.
        * It should process the event and update the Delivery based on event type.
        * Update delivery.estimatedArrivalDate
        * Set Delivery.DeliveryStatus to DELAYED
        * Update delivery.remainingDaysToNextHarbor
        * Optionally we can route the delivery to a different harbor, in that case we should update futureRoute
        * Also can block the harbor depending on event type
        * based on what the library tells us about the shortest path to that point from ship's current location.
     */
    protected abstract void processEventStart();

    /*
        * This method is called when the event is over.
        * Release any harbor, strait or passage that has been blocked by the event.
     */
    protected abstract void processEventEnd();

    enum EventStatus {
        PENDING, // The event has not started yet
        ACTIVE, // The event is currently active, no ending message has arrived
        COMPLETED  // The event has ended, we have received ending event from user
    }

    @Column(name = "event_status")
    @Enumerated(EnumType.STRING)
    protected EventStatus eventStatus = EventStatus.PENDING;

    @Column(name = "start_time")
    protected int remainingDaysToStart;

    public void startEvent() {
        eventStatus = EventStatus.ACTIVE;
        processEventStart();
    }

    public void endEvent() {
        eventStatus = EventStatus.COMPLETED;
        processEventEnd();
    }
}
