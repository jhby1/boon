package org.boon.core;

/**
 * @author Jonas Hallerby <jonas@strategicode.com>
 */
public interface Converter<T> {
    T parse(Class<T> clz, Object value);
}
