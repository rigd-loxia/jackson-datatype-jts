package nl.loxia.jts.parsers;

import static nl.loxia.jts.GeoJson.COORDINATES;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class PolygonParser extends BaseParser implements GeometryParser<Polygon> {

    public PolygonParser(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    public Polygon polygonFromJson(JsonNode node) {
        return polygonFromJsonArrayOfRings(node.get(COORDINATES));
    }

    public Polygon polygonFromJsonArrayOfRings(JsonNode arrayOfRings) {
        var shell = linearRingsFromJson(arrayOfRings.get(0));
        var holes = arrayOfRings.valueStream()
            .skip(1) //Skip the first one, which is the shell
            .map(this::linearRingsFromJson)
            .toArray(LinearRing[]::new);

        return geometryFactory.createPolygon(shell, holes);
    }

    @Override
    public Polygon geometryFromJson(JsonNode node) throws JacksonException {
        return polygonFromJson(node);
    }

    private LinearRing linearRingsFromJson(JsonNode coordinates) {
        assert coordinates.isArray() : "expected coordinates array";
        return geometryFactory.createLinearRing(PointParser.coordinatesFromJson(coordinates));
    }
}
