package org.boon.json;

import com.google.common.collect.Maps;
import org.boon.core.reflection.ErrorHandler;
import org.boon.json.implementation.ObjectMapperImpl;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Jonas Hallerby <jonas@strategicode.com>
 */
public class LoggingParserTest {
    static class MyClass {
        public String f1;
    }

    static class MyContainer {
        public int x;
        public List<MyClass> objects;
    }

    static class TestErrorHandler implements ErrorHandler {

        final Map<String, Object> result = Maps.newHashMap();
        int calls = 0;
        private Class<?> cls;

        @Override
        public void onMissingField(Class<?> cls, String key, Object value) {
            result.put(key, value);
            calls++;
            this.cls = cls;
        }
    }

    @Test
    public void test_logging_missing_object() {

        TestErrorHandler errorHandler = new TestErrorHandler();

        final MyContainer o = mapper(errorHandler).fromJson("{ x: 17, data: { k: 'v2'} }", MyContainer.class);
        assertEquals(17, o.x);
        assertNotNull(errorHandler.result.get("data"));
        assertEquals(1, errorHandler.calls);
        assertEquals(MyContainer.class, errorHandler.cls);
    }

    @Test
    public void test_logging_missing_deep_object() {

        TestErrorHandler errorHandler = new TestErrorHandler();

        final MyContainer o = mapper(errorHandler).fromJson("{ objects: [ { unkn: 'x1'} ] }", MyContainer.class);
        assertNotNull(errorHandler.result.get("unkn"));
        assertEquals(1, errorHandler.calls);
        assertEquals(MyClass.class, errorHandler.cls);
    }

    @Test
    public void test_logging_missing_string() {

        TestErrorHandler errorHandler = new TestErrorHandler();

        final MyClass o = mapper(errorHandler).fromJson("{ f1: 'v1', f2: 'v2'}", MyClass.class);
        assertEquals("v1", o.f1);
        assertEquals("v2", errorHandler.result.get("f2"));
        assertEquals(1, errorHandler.calls);
        assertEquals(MyClass.class, errorHandler.cls);
    }

    private ObjectMapperImpl mapper(TestErrorHandler errorHandler) {
        JsonParserFactory jsonParserFactory = new JsonParserFactory();
        jsonParserFactory.lax();
        jsonParserFactory.setErrorHandler(errorHandler);

        ObjectMapperImpl mapper = new ObjectMapperImpl(jsonParserFactory, new JsonSerializerFactory());
        return mapper;
    }
}
