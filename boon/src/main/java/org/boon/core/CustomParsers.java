package org.boon.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jonas Hallerby <jonas@strategicode.com>
 */
public class CustomParsers {

    final static ConcurrentHashMap<Class<?>, CustomParser> parsers = new ConcurrentHashMap<>();

    public static <T> T parseIfDefined(Class<T> clz, Object value) {
        final CustomParser parser = parsers.get(clz);
        if (parser != null) {
            return (T) parser.parse(clz, value);
        }
        return null;

    }

    public static <T> void register(Class<T> type, CustomParser<T> converter) {
        parsers.put(type, converter);
    }

}
