package nl.loxia.jts.parsers;

import org.locationtech.jts.geom.Geometry;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public interface GeometryParser<T extends Geometry> {

    T geometryFromJson(JsonNode node) throws JacksonException;

}
