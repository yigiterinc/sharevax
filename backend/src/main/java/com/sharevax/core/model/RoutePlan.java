package com.sharevax.core.model;

import com.sharevax.core.model.Harbor;
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
	private Harbor destinationHarbor;

	public RoutePlan(LineString routeHistory, LineString futureRoute, int duration, Harbor destinationHarbor) {
		this.routeHistory = routeHistory;
		this.futureRoute = futureRoute;
		this.duration = duration;
		this.destinationHarbor = destinationHarbor;
	}

	public RoutePlan(LineString routeHistory, LineString futureRoute) {
		this.routeHistory = routeHistory;
		this.futureRoute = futureRoute;
		this.duration = 0;
		this.destinationHarbor = new Harbor();
	}


}
