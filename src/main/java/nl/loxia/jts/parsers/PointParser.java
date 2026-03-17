package nl.loxia.jts.parsers;

import static nl.loxia.jts.GeoJson.COORDINATES;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class PointParser extends BaseParser implements GeometryParser<Point> {

    public PointParser(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    public static Coordinate coordinateFromJson(JsonNode array) {
        return switch (array.size()) {
            case 2 -> new Coordinate(
                array.get(0).asDouble(),
                array.get(1).asDouble());
            case 3 -> new Coordinate(
                array.get(0).asDouble(),
                array.get(1).asDouble(),
                array.get(2).asDouble());
            default -> throw new IllegalArgumentException("expecting coordinate array with single point [ x, y, |z| ]");
        };
    }

    public static Coordinate[] coordinatesFromJson(JsonNode array) {
        return array.valueStream()
            .map(PointParser::coordinateFromJson)
            .toArray(Coordinate[]::new);
    }

    public Point pointFromJson(JsonNode node) {
        return geometryFactory.createPoint(
            coordinateFromJson(node.get(COORDINATES))
        );
    }

    @Override
    public Point geometryFromJson(JsonNode node) throws JacksonException {
        return pointFromJson(node);
    }
}
