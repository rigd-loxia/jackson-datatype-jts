package nl.loxia.jts.parsers;

import static nl.loxia.jts.GeoJson.GEOMETRY_COLLECTION;
import static nl.loxia.jts.GeoJson.LINE_STRING;
import static nl.loxia.jts.GeoJson.MULTI_LINE_STRING;
import static nl.loxia.jts.GeoJson.MULTI_POINT;
import static nl.loxia.jts.GeoJson.MULTI_POLYGON;
import static nl.loxia.jts.GeoJson.POINT;
import static nl.loxia.jts.GeoJson.POLYGON;
import static nl.loxia.jts.GeoJson.TYPE;

import java.util.HashMap;
import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.exc.JsonNodeException;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class GenericGeometryParser extends BaseParser implements GeometryParser<Geometry> {

    private Map<String, GeometryParser> parsers;

    public GenericGeometryParser(GeometryFactory geometryFactory) {
        super(geometryFactory);
        parsers = new HashMap<String, GeometryParser>();
        parsers.put(POINT, new PointParser(geometryFactory));
        parsers.put(MULTI_POINT, new MultiPointParser(geometryFactory));
        parsers.put(LINE_STRING, new LineStringParser(geometryFactory));
        parsers.put(MULTI_LINE_STRING, new MultiLineStringParser(geometryFactory));
        parsers.put(POLYGON, new PolygonParser(geometryFactory));
        parsers.put(MULTI_POLYGON, new MultiPolygonParser(geometryFactory));
        parsers.put(GEOMETRY_COLLECTION, new GeometryCollectionParser(geometryFactory, this));
    }

    @Override
    public Geometry geometryFromJson(JsonNode node) throws JacksonException {
        String typeName = node.get(TYPE).asText();
        GeometryParser parser = parsers.get(typeName);
        if (parser != null) {
            return parser.geometryFromJson(node);
        }
        else {
            throw JsonNodeException.from(node, "Invalid geometry type: " + typeName);
        }
    }
}
