package com.sharevax.core.model.route;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateArrays;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public class LineStringSerializer extends JsonSerializer<LineString> {

	@Override
	public void serialize(LineString lineString, JsonGenerator jsonGenerator,
		SerializerProvider serializerProvider) throws IOException {

		jsonGenerator.writeStartArray();

		Coordinate[] coordinates = lineString.getCoordinates();
		for (Coordinate coordinate : coordinates) {
			double[] p = {coordinate.getX(), coordinate.getY()};
			jsonGenerator.writeArray(p, 0, 2);
		}

		jsonGenerator.writeEndArray();

	}
}
