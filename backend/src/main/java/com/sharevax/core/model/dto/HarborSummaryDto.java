package com.sharevax.core.model.dto;

import com.sharevax.core.model.Harbor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HarborSummaryDto {

    String name;
    Point coordinate;
    String countryName;

    public static HarborSummaryDto from(Harbor harbor) {
        return HarborSummaryDto.builder()
                .name(harbor.getName())
                .coordinate(harbor.getCoordinate())
                .countryName(harbor.getCountry().getName())
                .build();
    }
}
