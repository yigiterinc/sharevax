package com.sharevax.core.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BlockedChannel extends DelayingEvent {
    private ChannelOption channelOption;

    public BlockedChannel(ChannelOption channelOption) {
        super(channelOption.name());
        this.channelOption = channelOption;
    }

    @Override
    protected String getDescriptiveMessage() {
        return "Channel " + channelOption.name() + " is blocked";
    }

    @Override
    protected void processEventStart() {
        // TODO Auto-generated method stub
        // Update event status to active
        // Update channel if necessary
    }

    @Override
    protected void processEventEnd() {
        // TODO Auto-generated method stub
        // Update event status to completed
        // Unblock the channel and update channel status and route as necessary
    }

    enum ChannelOption {
        SUEZ,
        PANAMA,
        KIEL,
        CORINTH
    }
}