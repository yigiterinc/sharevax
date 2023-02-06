package com.sharevax.core.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("BLOCKED_CHANNEL")
public class BlockedChannel extends Event {

    @Enumerated(EnumType.STRING)
    @Column(name = "channel_name")
    private ChannelOption channelOption;

    public BlockedChannel(ChannelOption channelOption, int startTime) {
        super(channelOption.name(), startTime);
        this.channelOption = channelOption;
        super.setRemainingDaysToStart(startTime);
    }

    public BlockedChannel(String channelOption, int startTime) {
        super(channelOption, startTime);

        String[] channelOptions = Arrays.stream(ChannelOption.values()).map(Enum::name).toArray(String[]::new);
        if (Arrays.stream(channelOptions).noneMatch(channelOption::equals)) {
            throw new IllegalArgumentException("Invalid channel option");
        }

        this.channelOption = ChannelOption.valueOf(channelOption);
    }

    @Override
    protected String getDescriptiveMessage() {
        return "Channel " + channelOption.name() + " is blocked";
    }

    @Override
    protected void processEventStart() {
        eventStatus = EventStatus.ACTIVE;
        System.out.printf("Blocked channel event for channel %s is now active\n", subject);
    }

    @Override
    protected void processEventEnd() {
        // TODO Auto-generated method stub
        // Update event status to completed
        // Unblock the channel and update channel status and route as necessary
    }

    public enum ChannelOption {
        SUEZ,
        PANAMA,
        KIEL,
        CORINTH
    }
}