package com.bedatadriven.jackson.datatype.jts.parsers;

import static com.bedatadriven.jackson.datatype.jts.GeoJson.COORDINATES;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class LineStringParser extends BaseParser implements GeometryParser<LineString> {

    public LineStringParser(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    public LineString lineStringFromJson(JsonNode root) {
        return geometryFactory.createLineString(
            PointParser.coordinatesFromJson(root.get(COORDINATES)));
    }

    @Override
    public LineString geometryFromJson(JsonNode node) throws JacksonException {
        return lineStringFromJson(node);
    }
}
