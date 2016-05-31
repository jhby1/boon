package org.boon.core.reflection;

/**
 * @author Jonas Hallerby <jonas@strategicode.com>
 */
public interface ErrorHandler {
    void onMissingField(Class<?> cls, String key, Object value);
}
