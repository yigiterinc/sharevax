package com.sharevax.core.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sharevax.core.model.Harbor;
import com.sharevax.core.model.route.PointSerializer;
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

    @JsonSerialize(using = PointSerializer.class)
    Point coordinate;

    String countryName;

    String harborStatus;

    public static HarborSummaryDto from(Harbor harbor) {
        return HarborSummaryDto.builder()
                .name(harbor.getName())
                .coordinate(harbor.getCoordinate())
                .countryName(harbor.getCountry().getName())
                .harborStatus(harbor.getStatus().toString())
                .build();
    }
}
