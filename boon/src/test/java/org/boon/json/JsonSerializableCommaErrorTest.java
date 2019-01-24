package org.boon.json;

import org.boon.json.implementation.ObjectMapperImpl;
import org.junit.Test;

import java.io.Serializable;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class JsonSerializableCommaErrorTest {


    public static class Outer {
        Outer(Serializable inner) {
            this.inner = inner;
        }
        public Serializable inner;
    }

    public static class Inner implements Serializable {
        Inner() {}
        Inner(String s1) {
            this.s1 = s1;
        }

        public String s1;
    }


    ObjectMapperImpl mapper = new ObjectMapperImpl(new JsonParserFactory()
            .lax().setCheckDates(false), new JsonSerializerFactory().usePropertiesFirst());

    @Test
    public void mapper_serialize_empty() {
        String json = mapper.toJson(new Outer(new Inner()));
        assertNotNull(json);
        assertThat(json, not(containsString(",}")));
        mapper.fromJson(json, Outer.class);
    }

    @Test
    public void mapper_serialize_non_empty() {
        String json = mapper.toJson(new Outer(new Inner("test-string")));
        assertNotNull(json);
        assertThat(json, not(containsString(",}")));
        Outer outer = mapper.fromJson(json, Outer.class);
        assertEquals("test-string", ((Inner) outer.inner).s1);
    }


    JsonSerializer serializer = new JsonSerializerFactory().create();

    @Test
    public void simple_serializer_serialize_with_comma() {
        String json = serializer.serialize(new Outer(new Inner())).toString();
        assertNotNull(json);
        assertThat(json, not(containsString(",}")));
    }

}
