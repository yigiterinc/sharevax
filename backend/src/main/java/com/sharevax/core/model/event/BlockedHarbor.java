package com.sharevax.core.model.event;


import com.sharevax.core.model.Harbor;
import org.springframework.cglib.core.Block;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("BLOCKED_HARBOR")
public class BlockedHarbor extends DelayingEvent {

    @OneToOne
    private Harbor blockedHarbor;

    public BlockedHarbor() {
        super();
    }

    public BlockedHarbor(Harbor blockedHarbor) {
        super(blockedHarbor.getName());
        this.blockedHarbor = blockedHarbor;
    }

    public BlockedHarbor(Harbor blockedHarbor, int startTime, int endTime) {
        super(blockedHarbor.getName());
        this.blockedHarbor = blockedHarbor;
        this.startTime = startTime;
        this.endTime = endTime;
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
