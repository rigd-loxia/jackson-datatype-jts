package nl.loxia.jts;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.json.JsonMapper;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public abstract class BaseJtsModuleTest<T extends Geometry> {
    protected GeometryFactory gf = new GeometryFactory();
    private ObjectWriter writer;
    private ObjectMapper mapper;
    private T geometry;
    private String geometryAsGeoJson;

    protected BaseJtsModuleTest() {
    }

    @BeforeEach
    public void setup() {
        mapper = JsonMapper.builder()
            .addModule(new JtsModule())
            .build();
        writer = mapper.writer();
        geometry = createGeometry();
        geometryAsGeoJson = createGeometryAsGeoJson();
    }

    protected abstract Class<T> getType();

    protected abstract String createGeometryAsGeoJson();

    protected abstract T createGeometry();

    @Test
    public void shouldDeserializeConcreteType() throws Exception {
        T concreteGeometry = mapper.readValue(geometryAsGeoJson, getType());
        assertThat(toJson(concreteGeometry)).isEqualTo(geometryAsGeoJson);
    }

    @Test
    public void shouldDeserializeAsInterface() throws Exception {
        assertRoundTrip(geometry);
        assertThat(toJson(geometry)).isEqualTo(geometryAsGeoJson);
    }

    protected String toJson(Object value) {
        return writer.writeValueAsString(value);
    }

    protected void assertRoundTrip(T geom) {
        String json = writer.writeValueAsString(geom);
        System.out.println(json);
        Geometry regeom = mapper.readValue(json, Geometry.class);
        assertThat(geom.equalsExact(regeom)).isTrue();
    }
}
