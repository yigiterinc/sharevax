package com.sharevax.core.model.route;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.LineString;

@Getter
@Setter
@NoArgsConstructor
public class RoutePlan {

	private LineString routeHistory;
	private LineString futureRoute;
	private int duration;

	public RoutePlan(LineString routeHistory, LineString futureRoute, int duration) {
		this.routeHistory = routeHistory;
		this.futureRoute = futureRoute;
		this.duration = duration;
	}
}
