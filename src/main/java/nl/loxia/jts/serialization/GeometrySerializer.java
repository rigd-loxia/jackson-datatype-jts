package nl.loxia.jts.serialization;

import static nl.loxia.jts.GeoJson.COORDINATES;
import static nl.loxia.jts.GeoJson.GEOMETRIES;
import static nl.loxia.jts.GeoJson.TYPE;

import java.util.Arrays;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import nl.loxia.jts.GeometryTypes;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class GeometrySerializer extends ValueSerializer<Geometry> {

    @Override
    public void serialize(Geometry geometry, JsonGenerator jsonGenerator, SerializationContext context) {
        writeGeometry(jsonGenerator, geometry);
    }

    public void writeGeometry(JsonGenerator jsonGenerator, Geometry geometry) {
        switch (geometry) {
            case Polygon polygon -> writePolygon(jsonGenerator, polygon);
            case Point point -> writePoint(jsonGenerator, point);
            case MultiPoint multiPoint -> writeMultiPoint(jsonGenerator, multiPoint);
            case MultiPolygon multiPolygon -> writeMultiPolygon(jsonGenerator, multiPolygon);
            case LineString lineString -> writeLineString(jsonGenerator, lineString);
            case MultiLineString multiLineString -> writeMultiLineString(jsonGenerator, multiLineString);
            case GeometryCollection geometryCollection -> writeGeometryCollection(jsonGenerator, geometryCollection);
            default -> throw DatabindException.from(jsonGenerator, "Geometry type "
                + geometry.getClass().getName() + " cannot be serialized as GeoJSON." +
                "Supported types are: " + Arrays.asList(
                Point.class.getName(),
                LineString.class.getName(),
                Polygon.class.getName(),
                MultiPoint.class.getName(),
                MultiLineString.class.getName(),
                MultiPolygon.class.getName(),
                GeometryCollection.class.getName()));
        }
    }

    private void writeGeometryCollection(JsonGenerator jsonGenerator, GeometryCollection value) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringProperty(TYPE, GeometryTypes.GEOMETRY_COLLECTION.getStringValue());
        jsonGenerator.writeArrayPropertyStart(GEOMETRIES);

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writeGeometry(jsonGenerator, value.getGeometryN(i));
        }

        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }

    private void writeMultiPoint(JsonGenerator jsonGenerator, MultiPoint value) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringProperty(TYPE, GeometryTypes.MULTI_POINT.getStringValue());
        jsonGenerator.writeArrayPropertyStart(COORDINATES);

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writePointCoords(jsonGenerator, (Point) value.getGeometryN(i));
        }

        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }

    private void writeMultiLineString(JsonGenerator jsonGenerator, MultiLineString value) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringProperty(TYPE, GeometryTypes.MULTI_LINE_STRING.getStringValue());
        jsonGenerator.writeArrayPropertyStart(COORDINATES);

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writeLineStringCoords(jsonGenerator, (LineString) value.getGeometryN(i));
        }

        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }

    @Override
    public Class<Geometry> handledType() {
        return Geometry.class;
    }

    private void writeMultiPolygon(JsonGenerator jsonGenerator, MultiPolygon value) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringProperty(TYPE, GeometryTypes.MULTI_POLYGON.getStringValue());
        jsonGenerator.writeArrayPropertyStart(COORDINATES);

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writePolygonCoordinates(jsonGenerator, (Polygon) value.getGeometryN(i));
        }

        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }

    private void writePolygon(JsonGenerator jsonGenerator, Polygon value) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringProperty(TYPE, GeometryTypes.POLYGON.getStringValue());
        jsonGenerator.writeName(COORDINATES);
        writePolygonCoordinates(jsonGenerator, value);

        jsonGenerator.writeEndObject();
    }

    private void writePolygonCoordinates(JsonGenerator jsonGenerator, Polygon value) {
        jsonGenerator.writeStartArray();
        writeLineStringCoords(jsonGenerator, value.getExteriorRing());

        for (int i = 0; i < value.getNumInteriorRing(); ++i) {
            writeLineStringCoords(jsonGenerator, value.getInteriorRingN(i));
        }
        jsonGenerator.writeEndArray();
    }

    private void writeLineStringCoords(JsonGenerator jsonGenerator, LineString ring) {
        jsonGenerator.writeStartArray();
        for (int i = 0; i != ring.getNumPoints(); ++i) {
            Point p = ring.getPointN(i);
            writePointCoords(jsonGenerator, p);
        }
        jsonGenerator.writeEndArray();
    }

    private void writeLineString(JsonGenerator jsonGenerator, LineString lineString) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringProperty(TYPE, GeometryTypes.LINE_STRING.getStringValue());
        jsonGenerator.writeName(COORDINATES);
        writeLineStringCoords(jsonGenerator, lineString);
        jsonGenerator.writeEndObject();
    }

    private void writePoint(JsonGenerator jsonGenerator, Point p) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringProperty(TYPE, GeometryTypes.POINT.getStringValue());
        jsonGenerator.writeName(COORDINATES);
        writePointCoords(jsonGenerator, p);
        jsonGenerator.writeEndObject();
    }

    private void writePointCoords(JsonGenerator jsonGenerator, Point p) {
        jsonGenerator.writeStartArray();

        jsonGenerator.writeNumber(p.getCoordinate().x);
        jsonGenerator.writeNumber(p.getCoordinate().y);

        if (!Double.isNaN(p.getCoordinate().z)) {
            jsonGenerator.writeNumber(p.getCoordinate().z);
        }
        jsonGenerator.writeEndArray();
    }
}
