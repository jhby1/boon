package org.boon.core;

/**
 * @author Jonas Hallerby <jonas@strategicode.com>
 */
public interface CustomParser<T> {
    T parse(Class<T> clz, Object value);
}
