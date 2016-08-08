package org.boon.core;

import org.boon.Boon;
import org.junit.Test;

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

        private Primitive(String val) {
            // Simulate the case when we can't use the standard constructor
            throw new RuntimeException("Don't use this constructor");
        }
        private Primitive(String val, boolean x) {
            this.val = val;
        }
        public static Primitive create(String val) {
            return new Primitive(val, true);
        }
    }

    @Test
    public void convert_datetime() {
        Conversions.register(Primitive.class, new Converter<Primitive>() {
            @Override
            public Primitive parse(Class<Primitive> clz, Object value) {
                return Primitive.create((String) value);
            }
        });

        final Sample sample = Boon.fromJson("{p: \"x\"}", Sample.class);
        assertNotNull(sample);
        assertNotNull(sample.p);
        assertEquals("x", sample.p.val);
    }
}
