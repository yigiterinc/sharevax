package com.sharevax.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportEventDto {

    // One of: BlockedChannel, BlockedPassage, BlockedStrait, BlockedHarbor
    private String type;

    // name of the channel, passage, strait or harbor
    private String subject;

    private int remainingDaysToStart;

    public boolean isBlockedChannel() {
        return Objects.equals(type, "BlockedChannel");
    }

    public boolean isBlockedPassage() {
        return Objects.equals(type, "BlockedPassage");
    }

    public boolean isBlockedStrait() {
        return Objects.equals(type, "BlockedStrait");
    }

    public boolean isBlockedHarbor() {
        return Objects.equals(type, "BlockedHarbor");
    }
}
