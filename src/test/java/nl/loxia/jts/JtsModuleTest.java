package nl.loxia.jts;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

public class JtsModuleTest {
    private ObjectMapper mapper;

    @BeforeEach
    public void setupMapper() {
        mapper = JsonMapper.builder()
            .addModule(new JtsModule())
            .build();
    }

    @Test
    public void invalidGeometryType() {
        String json = "{\"type\":\"Singularity\",\"coordinates\":[]}";
        assertThrows(JacksonException.class, () -> mapper.readValue(json, Geometry.class));
    }

    @Test
    public void unsupportedGeometry() {
        Geometry unsupportedGeometry = EasyMock.createNiceMock("NonEuclideanGeometry", Geometry.class);
        EasyMock.replay(unsupportedGeometry);

        assertThrows(JacksonException.class, () -> mapper.writeValue(System.out, unsupportedGeometry));
    }
}
