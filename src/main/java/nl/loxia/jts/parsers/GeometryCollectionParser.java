package nl.loxia.jts.parsers;

import static nl.loxia.jts.GeoJson.GEOMETRIES;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class GeometryCollectionParser extends BaseParser implements GeometryParser<GeometryCollection> {

    private GenericGeometryParser genericGeometriesParser;

    public GeometryCollectionParser(GeometryFactory geometryFactory, GenericGeometryParser genericGeometriesParser) {
        super(geometryFactory);
        this.genericGeometriesParser = genericGeometriesParser;
    }

    private Geometry[] geometriesFromJson(JsonNode arrayOfGeoms) throws JacksonException {
        Geometry[] items = new Geometry[arrayOfGeoms.size()];
        for (int i = 0; i < arrayOfGeoms.size(); ++i) {
            items[i] = genericGeometriesParser.geometryFromJson(arrayOfGeoms.get(i));
        }
        return items;
    }

    @Override
    public GeometryCollection geometryFromJson(JsonNode node) throws JacksonException {
        return geometryFactory.createGeometryCollection(
            geometriesFromJson(node.get(GEOMETRIES)));
    }
}
