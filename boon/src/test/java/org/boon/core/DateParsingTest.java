package org.boon.core;

import org.boon.json.JsonParserFactory;
import org.boon.json.JsonSerializerFactory;
import org.boon.json.implementation.ObjectMapperImpl;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Jonas Hallerby <jonas.hallerby@edstart.com.au>
 */
public class DateParsingTest {
    private final ObjectMapperImpl mapper;

    public DateParsingTest() {
        JsonParserFactory pf = new JsonParserFactory().lax().setCheckDates(false);
        mapper = new ObjectMapperImpl(pf, new JsonSerializerFactory());

    }

    static class Sample {
        Object d;
    }

    @Test
    public void parse_map_date_as_string() {
        String d = "2017-03-27T04:01:54.778Z";
        String json = "{'d':'" + d + "'}";
        final Map map = mapper.fromJson(json, Map.class);
        assertEquals(d, map.get("d"));

    }

    @Test
    public void parse_object_date_as_string() {
        String d = "2017-03-27T04:01:54.778Z";
        String json = "{'d':'" + d + "'}";
        final Sample sample = mapper.fromJson(json, Sample.class);
        assertEquals(d, sample.d.toString());
    }

    static class Sample2 {
        Map<String, Object> m;
    }

    @Test
    public void parse_object_map_date_as_string() {
        String d = "2017-03-27T04:01:54.778Z";
        String json = "{'m': {'d':'" + d + "'}}";
        final Sample2 sample = mapper.fromJson(json, Sample2.class);
        Value dValue = (Value) sample.m.get("d");
        assertEquals(d, dValue.toValue());
    }

}
