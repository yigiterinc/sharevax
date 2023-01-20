package com.sharevax.core.model.event;

import com.sharevax.core.util.CasingUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Getter
@Setter
@DiscriminatorValue("BLOCKED_PASSAGE")
public class BlockedPassage extends Event {

    @Enumerated(EnumType.STRING)
    @Column(name = "passage_name")
    private PassageOption passage;

    public BlockedPassage() {
        super();
    }

    public BlockedPassage(PassageOption passage) {
        super(passage.name(), 0);
        this.passage = passage;
    }

    public BlockedPassage(PassageOption passage, int startTime) {
        super(passage.name(), startTime);
        this.passage = passage;
        this.remainingDaysToStart = startTime;
    }

    public BlockedPassage(String passageOption, int remainingDaysToStart) {
        super(passageOption, remainingDaysToStart);
        String[] channelOptions = Arrays.stream(BlockedPassage.PassageOption.values()).map(Enum::name).toArray(String[]::new);
        if (Arrays.stream(channelOptions).noneMatch(passageOption::equals)) {
            throw new IllegalArgumentException("Invalid passage option");
        }
        this.passage = PassageOption.valueOf(subject);
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

    public enum PassageOption {
        NORTHWEST,
        NORTHEAST,
    }

}
