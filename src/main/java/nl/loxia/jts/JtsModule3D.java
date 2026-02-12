package nl.loxia.jts;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import nl.loxia.jts.parsers.GenericGeometryParser;
import nl.loxia.jts.parsers.GeometryCollectionParser;
import nl.loxia.jts.parsers.LineStringParser;
import nl.loxia.jts.parsers.MultiLineStringParser;
import nl.loxia.jts.parsers.MultiPointParser;
import nl.loxia.jts.parsers.MultiPolygonParser;
import nl.loxia.jts.parsers.PointParser;
import nl.loxia.jts.parsers.PolygonParser;
import nl.loxia.jts.serialization.GeometryDeserializer;
import nl.loxia.jts.serialization.GeometrySerializer;
import tools.jackson.core.Version;
import tools.jackson.databind.module.SimpleModule;

public class JtsModule3D extends SimpleModule {

    public JtsModule3D() {
        this(new GeometryFactory());
    }

    public JtsModule3D(GeometryFactory geometryFactory) {
        super("JtsModule3D", new Version(1, 0, 0, null, "nl.loxia.jts", "jackson-datatype-jts"));

        addSerializer(Geometry.class, new GeometrySerializer());
        GenericGeometryParser genericGeometryParser = new GenericGeometryParser(geometryFactory);
        addDeserializer(Geometry.class, new GeometryDeserializer<Geometry>(genericGeometryParser));
        addDeserializer(Point.class, new GeometryDeserializer<Point>(new PointParser(geometryFactory)));
        addDeserializer(MultiPoint.class, new GeometryDeserializer<MultiPoint>(new MultiPointParser(geometryFactory)));
        addDeserializer(LineString.class, new GeometryDeserializer<LineString>(new LineStringParser(geometryFactory)));
        addDeserializer(MultiLineString.class,
            new GeometryDeserializer<MultiLineString>(new MultiLineStringParser(geometryFactory)));
        addDeserializer(Polygon.class, new GeometryDeserializer<Polygon>(new PolygonParser(geometryFactory)));
        addDeserializer(MultiPolygon.class, new GeometryDeserializer<MultiPolygon>(new MultiPolygonParser(geometryFactory)));
        addDeserializer(GeometryCollection.class,
            new GeometryDeserializer<GeometryCollection>(new GeometryCollectionParser(geometryFactory, genericGeometryParser)));
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
    }
}
