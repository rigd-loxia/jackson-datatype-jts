package nl.loxia.jts.parsers;

import static nl.loxia.jts.GeoJson.COORDINATES;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class MultiLineStringParser extends BaseParser implements GeometryParser<MultiLineString> {

    public MultiLineStringParser(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    public MultiLineString multiLineStringFromJson(JsonNode root) {
        return geometryFactory.createMultiLineString(
            lineStringsFromJson(root.get(COORDINATES)));
    }

    @Override
    public MultiLineString geometryFromJson(JsonNode node) throws JacksonException {
        return multiLineStringFromJson(node);
    }

    private LineString[] lineStringsFromJson(JsonNode array) {
        return array.valueStream()
            .map(node -> geometryFactory.createLineString(PointParser.coordinatesFromJson(node)))
            .toArray(LineString[]::new);
    }
}
