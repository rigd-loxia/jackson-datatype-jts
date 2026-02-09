package com.bedatadriven.jackson.datatype.jts.parsers;

import com.vividsolutions.jts.geom.Geometry;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public interface GeometryParser<T extends Geometry> {

    T geometryFromJson(JsonNode node) throws JacksonException;

}
