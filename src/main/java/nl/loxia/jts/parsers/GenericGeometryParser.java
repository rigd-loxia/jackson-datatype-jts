package nl.loxia.jts.parsers;

import static nl.loxia.jts.GeoJson.GEOMETRY_COLLECTION;
import static nl.loxia.jts.GeoJson.LINE_STRING;
import static nl.loxia.jts.GeoJson.MULTI_LINE_STRING;
import static nl.loxia.jts.GeoJson.MULTI_POINT;
import static nl.loxia.jts.GeoJson.MULTI_POLYGON;
import static nl.loxia.jts.GeoJson.POINT;
import static nl.loxia.jts.GeoJson.POLYGON;
import static nl.loxia.jts.GeoJson.TYPE;

import java.util.Map;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.exc.JsonNodeException;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class GenericGeometryParser extends BaseParser implements GeometryParser<Geometry> {

    private final Map<String, GeometryParser<?>> parsers;

    public GenericGeometryParser(GeometryFactory geometryFactory) {
        super(geometryFactory);
        this.parsers = Map.of(
            POINT, new PointParser(geometryFactory),
            MULTI_POINT, new MultiPointParser(geometryFactory),
            LINE_STRING, new LineStringParser(geometryFactory),
            MULTI_LINE_STRING, new MultiLineStringParser(geometryFactory),
            POLYGON, new PolygonParser(geometryFactory),
            MULTI_POLYGON, new MultiPolygonParser(geometryFactory),
            GEOMETRY_COLLECTION, new GeometryCollectionParser(geometryFactory, this)
        );
    }

    @Override
    public Geometry geometryFromJson(JsonNode node) throws JacksonException {
        var typeName = node.get(TYPE).asString();
        var geometryParser = parsers.get(typeName);
        if (geometryParser != null) {
            return geometryParser.geometryFromJson(node);
        }
        else {
            throw JsonNodeException.from(node, "Invalid geometry type: " + typeName);
        }
    }
}
