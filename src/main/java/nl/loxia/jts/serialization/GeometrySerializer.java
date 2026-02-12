package nl.loxia.jts.serialization;

import static nl.loxia.jts.GeoJson.COORDINATES;
import static nl.loxia.jts.GeoJson.GEOMETRIES;
import static nl.loxia.jts.GeoJson.GEOMETRY_COLLECTION;
import static nl.loxia.jts.GeoJson.LINE_STRING;
import static nl.loxia.jts.GeoJson.MULTI_LINE_STRING;
import static nl.loxia.jts.GeoJson.MULTI_POINT;
import static nl.loxia.jts.GeoJson.MULTI_POLYGON;
import static nl.loxia.jts.GeoJson.POINT;
import static nl.loxia.jts.GeoJson.POLYGON;
import static nl.loxia.jts.GeoJson.TYPE;

import java.io.IOException;
import java.util.Arrays;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.exc.JacksonIOException;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class GeometrySerializer extends ValueSerializer<Geometry> {

    @Override
    public void serialize(Geometry value, JsonGenerator jgen, SerializationContext context) {
        try {
            writeGeometry(jgen, value);
        }
        catch (IOException e) {
            throw JacksonIOException.construct(e);
        }
    }

    public void writeGeometry(JsonGenerator jgen, Geometry value)
        throws IOException {
        if (value instanceof Polygon) {
            writePolygon(jgen, (Polygon) value);

        }
        else if (value instanceof Point) {
            writePoint(jgen, (Point) value);

        }
        else if (value instanceof MultiPoint) {
            writeMultiPoint(jgen, (MultiPoint) value);

        }
        else if (value instanceof MultiPolygon) {
            writeMultiPolygon(jgen, (MultiPolygon) value);

        }
        else if (value instanceof LineString) {
            writeLineString(jgen, (LineString) value);

        }
        else if (value instanceof MultiLineString) {
            writeMultiLineString(jgen, (MultiLineString) value);

        }
        else if (value instanceof GeometryCollection) {
            writeGeometryCollection(jgen, (GeometryCollection) value);

        }
        else {
            throw DatabindException.from(jgen, "Geometry type "
                + value.getClass().getName() + " cannot be serialized as GeoJSON." +
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

    private void writeGeometryCollection(JsonGenerator jgen, GeometryCollection value) throws
        IOException {
        jgen.writeStartObject();
        jgen.writeStringProperty(TYPE, GEOMETRY_COLLECTION);
        jgen.writeArrayPropertyStart(GEOMETRIES);

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writeGeometry(jgen, value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    private void writeMultiPoint(JsonGenerator jgen, MultiPoint value)
        throws IOException {
        jgen.writeStartObject();
        jgen.writeStringProperty(TYPE, MULTI_POINT);
        jgen.writeArrayPropertyStart(COORDINATES);

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writePointCoords(jgen, (Point) value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    private void writeMultiLineString(JsonGenerator jgen, MultiLineString value)
        throws IOException {
        jgen.writeStartObject();
        jgen.writeStringProperty(TYPE, MULTI_LINE_STRING);
        jgen.writeArrayPropertyStart(COORDINATES);

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writeLineStringCoords(jgen, (LineString) value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    @Override
    public Class<Geometry> handledType() {
        return Geometry.class;
    }

    private void writeMultiPolygon(JsonGenerator jgen, MultiPolygon value)
        throws IOException {
        jgen.writeStartObject();
        jgen.writeStringProperty(TYPE, MULTI_POLYGON);
        jgen.writeArrayPropertyStart(COORDINATES);

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writePolygonCoordinates(jgen, (Polygon) value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    private void writePolygon(JsonGenerator jgen, Polygon value)
        throws IOException {
        jgen.writeStartObject();
        jgen.writeStringProperty(TYPE, POLYGON);
        jgen.writeName(COORDINATES);
        writePolygonCoordinates(jgen, value);

        jgen.writeEndObject();
    }

    private void writePolygonCoordinates(JsonGenerator jgen, Polygon value)
        throws IOException {
        jgen.writeStartArray();
        writeLineStringCoords(jgen, value.getExteriorRing());

        for (int i = 0; i < value.getNumInteriorRing(); ++i) {
            writeLineStringCoords(jgen, value.getInteriorRingN(i));
        }
        jgen.writeEndArray();
    }

    private void writeLineStringCoords(JsonGenerator jgen, LineString ring)
        throws IOException {
        jgen.writeStartArray();
        for (int i = 0; i != ring.getNumPoints(); ++i) {
            Point p = ring.getPointN(i);
            writePointCoords(jgen, p);
        }
        jgen.writeEndArray();
    }

    private void writeLineString(JsonGenerator jgen, LineString lineString)
        throws IOException {
        jgen.writeStartObject();
        jgen.writeStringProperty(TYPE, LINE_STRING);
        jgen.writeName(COORDINATES);
        writeLineStringCoords(jgen, lineString);
        jgen.writeEndObject();
    }

    private void writePoint(JsonGenerator jgen, Point p)
        throws IOException {
        jgen.writeStartObject();
        jgen.writeStringProperty(TYPE, POINT);
        jgen.writeName(COORDINATES);
        writePointCoords(jgen, p);
        jgen.writeEndObject();
    }

    private void writePointCoords(JsonGenerator jgen, Point p)
        throws IOException {
        jgen.writeStartArray();

        jgen.writeNumber(p.getCoordinate().x);
        jgen.writeNumber(p.getCoordinate().y);

        if (!Double.isNaN(p.getCoordinate().z)) {
            jgen.writeNumber(p.getCoordinate().z);
        }
        jgen.writeEndArray();
    }

}
