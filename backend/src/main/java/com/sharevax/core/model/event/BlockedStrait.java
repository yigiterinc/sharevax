package com.sharevax.core.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.sharevax.core.util.CasingUtil;

import javax.persistence.*;
import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("BLOCKED_STRAIT")
public class BlockedStrait extends Event {

    @Column(name = "strait_name")
    @Enumerated(EnumType.STRING)
    private StraitOption straitOption;

    public BlockedStrait(StraitOption straitOption, int startTime) {
        super(straitOption.name(), startTime);
        this.straitOption = straitOption;
    }

    public BlockedStrait(String straitOption, int startTime) {
        super(straitOption, startTime);
        String[] straitOptions = Arrays.stream(BlockedStrait.StraitOption.values()).map(Enum::name).toArray(String[]::new);
        if (Arrays.stream(straitOptions).noneMatch(straitOption::equals)) {
            throw new IllegalArgumentException("Invalid passage option");
        }

        this.straitOption = StraitOption.valueOf(subject);
    }

    @Override
    protected String getDescriptiveMessage() {
        String straitName = CasingUtil.toCamelCase(straitOption.name());
        return "Strait " + straitName + " is blocked";
    }

    @Override
    protected void processEventStart() {
        // TODO Auto-generated method stub
        // Update event status to active
        // Update delivery if necessary
    }

    @Override
    protected void processEventEnd() {
        // TODO Auto-generated method stub
        // Update event status to completed
        // Unblock the strait and update delivery status and route as necessary
    }

    enum StraitOption {
        MALACCA,
        GIBRALTAR,
        DOVER,
        BERING,
        MAGELLAN,
        BAB_EL_MANDEB,
    }
}
