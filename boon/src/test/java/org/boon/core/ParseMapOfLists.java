package org.boon.core;

import org.boon.Boon;
import org.boon.core.reflection.ErrorHandler;
import org.boon.json.JsonParserAndMapper;
import org.boon.json.JsonParserFactory;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Jonas Hallerby <jonas.hallerby@edstart.com.au>
 */
public class ParseMapOfLists {

    public static class Container {
        public Map<Integer, List<String>> data1;
    }

    public static class ContainerEx {
        public Map<Integer, List<Item>> data1;
    }

    public static class ListContainer {
        public List<Item> data;
    }

    public static class Item {
        public String label;
    }

    @Test
    public void parse_empty_map_of_lists() {
        String json = "{ data: {}}";
        assertNotNull(Boon.fromJson(json, Container.class));
    }

    final String simple_map_of_lists = "{ data1: { 17: ['x']}}";
    final String complex_map_of_lists = "{ data1: { 99: [ { label: 'x'}]}}";

    @Test
    public void parse_simple_map_of_lists() {
        final Container container = Boon.fromJson(simple_map_of_lists, Container.class);
        check_simple_map_of_lists(container);
    }

    @Test
    public void parse_map_of_item_lists() {
        final ContainerEx container = Boon.fromJson(complex_map_of_lists, ContainerEx.class);
        assertNotNull(container);
        assertNotNull(container.data1);
        assertEquals(1, container.data1.size());
        final List<Item> strings = container.data1.get(99);
        assertNotNull(strings);
        assertEquals(1, strings.size());
        assertEquals("x", strings.get(0).label);
    }


    private void check_simple_map_of_lists(Container container) {
        assertNotNull(container);
        assertNotNull(container.data1);
        assertEquals(1, container.data1.size());
        final List<String> strings = container.data1.get(17);
        assertNotNull(strings);
        assertEquals(1, strings.size());
        assertEquals("x", strings.get(0));
    }

    @Test
    public void create_complex() {
        JsonParserFactory jsonParserFactory = new JsonParserFactory();
        jsonParserFactory.lax();
        jsonParserFactory.setErrorHandler(new ErrorHandler() {
            @Override
            public void onMissingField(Class<?> cls, String key, Object value) {

            }
        });
        final JsonParserAndMapper mapper = jsonParserFactory.create();
        final Container container = mapper.parse(Container.class, simple_map_of_lists);
        check_simple_map_of_lists(container);

    }

    @Test
    public void parse_data_list() {
        final ListContainer listContainer = Boon.fromJson("{ data: [ { label: 'x'}] }", ListContainer.class);
        assertNotNull(listContainer.data);
        assertEquals(1, listContainer.data.size());
        assertEquals("x", listContainer.data.get(0).label);

    }

}
