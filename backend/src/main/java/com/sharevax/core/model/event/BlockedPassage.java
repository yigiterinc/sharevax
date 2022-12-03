package com.sharevax.core.model.event;

import com.sharevax.core.util.CasingUtil;

public class BlockedPassage extends DelayingEvent {
    private PassageOption passage;

    public BlockedPassage() {
        super();
    }

    public BlockedPassage(PassageOption passage) {
        super(passage.name());
        this.passage = passage;
    }

    public BlockedPassage(PassageOption passage, int startTime, int endTime) {
        super();
        this.passage = passage;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    protected String getDescriptiveMessage() {
        String passageName = CasingUtil.toCamelCase(passage.name());
        return "Passage " + passageName + " is blocked";
    }

    @Override
    protected void processEventStart() {
        // TODO Auto-generated method stub
        // Actually block the passage and update delivery
    }

    @Override
    protected void processEventEnd() {
        // TODO Auto-generated method stub
        // Unblock the passage and update delivery status and route as necessary
    }

    enum PassageOption {
        NORTHWEST,
        NORTHEAST,
    }

}
