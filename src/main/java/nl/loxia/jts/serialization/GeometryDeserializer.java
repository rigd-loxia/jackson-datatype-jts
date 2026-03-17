package nl.loxia.jts.serialization;

import org.locationtech.jts.geom.Geometry;

import lombok.RequiredArgsConstructor;
import nl.loxia.jts.parsers.GeometryParser;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

/**
 * Created by mihaildoronin on 11/11/15.
 */
@RequiredArgsConstructor
public class GeometryDeserializer<T extends Geometry> extends ValueDeserializer<T> {

    private final GeometryParser<T> geometryParser;

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        var rootNode = jsonParser.<JsonNode>readValueAsTree();
        return geometryParser.geometryFromJson(rootNode);
    }
}
