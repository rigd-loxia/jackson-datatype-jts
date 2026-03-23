package nl.loxia.jts.parsers;

import static nl.loxia.jts.GeoJson.TYPE;

import java.util.Map;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import nl.loxia.jts.GeometryTypes;
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
            GeometryTypes.POINT.getStringValue(), new PointParser(geometryFactory),
            GeometryTypes.MULTI_POINT.getStringValue(), new MultiPointParser(geometryFactory),
            GeometryTypes.LINE_STRING.getStringValue(), new LineStringParser(geometryFactory),
            GeometryTypes.MULTI_LINE_STRING.getStringValue(), new MultiLineStringParser(geometryFactory),
            GeometryTypes.POLYGON.getStringValue(), new PolygonParser(geometryFactory),
            GeometryTypes.MULTI_POLYGON.getStringValue(), new MultiPolygonParser(geometryFactory),
            GeometryTypes.GEOMETRY_COLLECTION.getStringValue(), new GeometryCollectionParser(geometryFactory, this)
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
