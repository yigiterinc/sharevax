package com.sharevax.core.model.route;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.locationtech.jts.geom.Point;

public class PointSerializer extends JsonSerializer<Point> {

	@Override
	public void serialize(Point point, JsonGenerator jsonGenerator,
		SerializerProvider serializerProvider) throws IOException {
		double[] p = {point.getX(), point.getY()};
		jsonGenerator.writeArray(p, 0, 2);
	}
}
