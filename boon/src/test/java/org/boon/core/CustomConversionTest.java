package org.boon.core;

import com.google.common.collect.Lists;
import org.boon.Boon;
import org.boon.core.reflection.ErrorHandler;
import org.boon.json.JsonParserAndMapper;
import org.boon.json.JsonParserFactory;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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

    public static class ListWrapper<T> {
        public final Collection<T> list;

        public ListWrapper(Collection<T> list) {
            this.list = list;
        }
    }

    public void use_static_parser() {
        CustomParsers.register(Primitive.class, new CustomParser<Primitive>() {
            @Override
            public Primitive parse(Class<Primitive> clz, Object value) {
                return Primitive.create((String) value, "customParser1");
            }
        });
    }

    final JsonParserAndMapper complexMapper;
    public CustomConversionTest() {
        JsonParserFactory jsonParserFactory = new JsonParserFactory();
        jsonParserFactory.lax();
        jsonParserFactory.setErrorHandler(new ErrorHandler() {
            @Override
            public void onMissingField(Class<?> cls, String key, Object value) {

            }
        });
        complexMapper = jsonParserFactory.create();
    }

    public void use_dynamic_parser() {
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
        CustomParsers.register(ListWrapper.class, new CustomParser<ListWrapper>() {
            @Override
            public ListWrapper parse(Class<ListWrapper> clz, Object value) {
                if (value instanceof Collection) {
                    return new ListWrapper((Collection) value);
                }
                throw new RuntimeException("Unsupported type: " + value.getClass());
            }
        });

    }

    private final String sample1json = "{p: \"x\"}";
    private final String sample2json = "{p: {\"val\": \"x\"}}";

    @Test
    public void simple_parse_string_to_item() {
        use_static_parser();

        final Sample sample = Boon.fromJson(sample1json, Sample.class);
        checkSample(sample, "customParser1");
    }

    @Test
    public void complex_parse_string_to_item() {
        use_static_parser();

        final Sample sample = complexMapper.parse(Sample.class, sample1json);
        checkSample(sample, "customParser1");
    }

    private void checkSample(Sample sample, String source) {
        assertNotNull(sample);
        assertNotNull(sample.p);
        assertEquals("x", sample.p.val);
        assertEquals(source, sample.p.source);

    }

    @Test
    public void simple_parse_map_to_item() {
        use_dynamic_parser();

        final Sample sample = Boon.fromJson(sample2json, Sample.class);
        checkSample(sample, "customParser2");

    }

    @Test
    public void complex_parse_map_to_item() {
        use_dynamic_parser();

        final Sample sample = complexMapper.parse(Sample.class, sample2json);
        checkSample(sample, "customParser2");
    }



    @Test
    public void parse_empty_list() {
        use_dynamic_parser();
        assertEquals(0, complexMapper.parse(ListWrapper.class, "[]").list.size());
    }

    @Test
    public void parse_string_list_one_entry() {
        use_dynamic_parser();
        ListWrapper p = complexMapper.parse(ListWrapper.class, "['a']");
        assertEquals(1, p.list.size());
        assertEquals("a", p.list.iterator().next().toString());
    }

}
