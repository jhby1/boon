package org.boon.core;

import org.boon.Boon;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Jonas Hallerby <jonas@strategicode.com>
 */
public class CustomConversionTest {

    public static class Sample {
        Primitive p;
    }

    public static class Primitive {
        public final String val;
        public final String source;

        private Primitive(String val) {
            // Simulate the case when we can't use the standard constructor
            throw new RuntimeException("Don't use this constructor");
        }
        private Primitive(String val, String source) {
            this.val = val;
            this.source = source;
        }
        public static Primitive create(String val, String source) {
            return new Primitive(val, source);
        }
    }

    @Test
    public void parse_string_to_item() {
        CustomParsers.register(Primitive.class, new CustomParser<Primitive>() {
            @Override
            public Primitive parse(Class<Primitive> clz, Object value) {
                return Primitive.create((String) value, "customParser1");
            }
        });

        final Sample sample = Boon.fromJson("{p: \"x\"}", Sample.class);
        assertNotNull(sample);
        assertNotNull(sample.p);
        assertEquals("x", sample.p.val);
        assertEquals("customParser1", sample.p.source);
    }

    @Test
    public void parse_map_to_item() {
        CustomParsers.register(Primitive.class, new CustomParser<Primitive>() {
            @Override
            public Primitive parse(Class<Primitive> clz, Object value) {
                if (value instanceof Map) {
                    final Object x = ((Map) value).get("val");
                    return Primitive.create(x.toString(), "customParser2");
                }
                throw new RuntimeException("Unexpected input type");
            }
        });

        final Sample sample = Boon.fromJson("{p: {\"val\": \"x\"}}", Sample.class);
        assertNotNull(sample);
        assertNotNull(sample.p);
        assertEquals("x", sample.p.val);
        assertEquals("customParser2", sample.p.source);

    }

}
