
# Jackson-datatype-jts

[![Maven Release](https://img.shields.io/maven-central/v/nl.loxia.jts/jackson-datatype-jts.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22nl.loxia.jts%22%20AND%20a%3A%22jackson-datatype-jts%22)

Jackson Module which provides custom serializers and deserializers for
[JTS Geometry](https://github.com/locationtech/jts) objects
using the [GeoJSON format](http://www.geojson.org/geojson-spec.html)

## Installation 

Releases of jackson-datatype-jts are available on Maven Central.

### Maven

To use the module in Maven-based projects, use following dependency:

```xml
<dependency>
  <groupId>nl.loxia.jts</groupId>
  <artifactId>jackson-datatype-jts</artifactId>
  <version>0.0.3</version>
</dependency>    
```

## Usage

### Registering module

To use JTS geometry datatypes with Jackson, you will first need to register the module first (same as
with all Jackson datatype modules):

```java
 JsonMapper.builder()
    .addModules(new GeoJsonModule())
    .build();
```

### Reading and Writing Geometry types

After registering JTS module, [Jackson Databind](https://github.com/FasterXML/jackson-databind)
will be able to write Geometry instances as GeoJSON and
and read GeoJSON geometries as JTS Geometry objects.

To write a Point object as GeoJSON:

```java
GeometryFactory gf = new GeometryFactory();
Point point = gf.createPoint(new Coordinate(1.2345678, 2.3456789));
String geojson = objectMapper.writeValueAsString(point);
```

You can also read GeoJSON in as JTS geometry objects:

```java
InputStream in;
Point point = mapper.readValue(in, Point.class);
```
