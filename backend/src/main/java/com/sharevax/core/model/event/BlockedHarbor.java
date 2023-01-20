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
    private Harbor blockedHarbor;

    public BlockedHarbor() {
        super();
    }

    public BlockedHarbor(Harbor blockedHarbor) {
        super(blockedHarbor.getName(), 0);
        this.blockedHarbor = blockedHarbor;
    }

    public BlockedHarbor(Harbor blockedHarbor, int startTime) {
        super(blockedHarbor.getName(), startTime);
        this.blockedHarbor = blockedHarbor;
        this.remainingDaysToStart = startTime;
    }

    @Override
    protected String getDescriptiveMessage() {
        return "Harbor " + blockedHarbor.getName() + " is blocked";
    }

    @Override
    protected void processEventStart() {
        // TODO Auto-generated method stub
        // Actually block the harbor and update delivery
    }

    @Override
    protected void processEventEnd() {
        // TODO Auto-generated method stub
        // Unblock the harbor and update delivery status and route as necessary
    }
}
