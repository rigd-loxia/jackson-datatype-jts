package nl.loxia.jts.parsers;

import static nl.loxia.jts.GeoJson.COORDINATES;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class MultiPolygonParser extends BaseParser implements GeometryParser<MultiPolygon> {

    private final PolygonParser helperParser;

    public MultiPolygonParser(GeometryFactory geometryFactory) {
        super(geometryFactory);
        this.helperParser = new PolygonParser(geometryFactory);
    }

    public MultiPolygon multiPolygonFromJson(JsonNode root) {
        return geometryFactory.createMultiPolygon(
            polygonsFromJson(root.get(COORDINATES))
        );
    }

    @Override
    public MultiPolygon geometryFromJson(JsonNode node) throws JacksonException {
        return multiPolygonFromJson(node);
    }

    private Polygon[] polygonsFromJson(JsonNode arrayOfPolygons) {
        return arrayOfPolygons.valueStream()
            .map(helperParser::polygonFromJsonArrayOfRings)
            .toArray(Polygon[]::new);
    }
}
