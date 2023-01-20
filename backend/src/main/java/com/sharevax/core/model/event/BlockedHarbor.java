package com.sharevax.core.model.event;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharevax.core.model.Harbor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@DiscriminatorValue("BLOCKED_HARBOR")
public class BlockedHarbor extends Event {

    @OneToOne
    @JsonIgnore
    private Harbor harbor;

    public BlockedHarbor() {
        super();
    }

    public BlockedHarbor(Harbor harbor) {
        super(harbor.getName(), 0);
        this.harbor = harbor;
    }

    public BlockedHarbor(Harbor harbor, int startTime) {
        super(harbor.getName(), startTime);
        this.harbor = harbor;
        this.remainingDaysToStart = startTime;
    }

    @Override
    protected String getDescriptiveMessage() {
        return "Harbor " + harbor.getName() + " is blocked";
    }

    @Override
    protected void processEventStart() {
        harbor.setStatus(Harbor.HarborStatus.CLOSED);
        eventStatus = EventStatus.ACTIVE;
    }

    @Override
    protected void processEventEnd() {
        harbor.setStatus(Harbor.HarborStatus.AVAILABLE);
        eventStatus = EventStatus.COMPLETED;
        // Unblock the harbor and update delivery status and route as necessary
    }
}
