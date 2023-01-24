package com.sharevax.core.serializer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.LineString;

@Getter
@Setter
@NoArgsConstructor
public class RoutePlanDto {

	private LineString routeHistory;
	private LineString futureRoute;
	private int duration;

	public RoutePlanDto(LineString routeHistory, LineString futureRoute, int duration) {
		this.routeHistory = routeHistory;
		this.futureRoute = futureRoute;
		this.duration = duration;
	}
}
