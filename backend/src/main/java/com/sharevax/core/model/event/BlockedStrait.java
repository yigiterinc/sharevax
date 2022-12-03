package com.sharevax.core.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.sharevax.core.util.CasingUtil;

@Getter
@Setter
@NoArgsConstructor
public class BlockedStrait extends DelayingEvent {
    private StraitOption straitOption;

    public BlockedStrait(StraitOption straitOption) {
        super(straitOption.name());
        this.straitOption = straitOption;
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
