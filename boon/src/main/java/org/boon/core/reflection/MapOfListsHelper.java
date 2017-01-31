package org.boon.core.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Jonas Hallerby <jonas.hallerby@edstart.com.au>
 */
public class MapOfListsHelper {
    public static Class getValueType(Type typeArg) {
        if (typeArg instanceof ParameterizedType) {
            return null;
        }
        return (Class) typeArg;
    }

    public static Class getInnerType(Type typeArg) {
        if (typeArg instanceof ParameterizedType) {
            ParameterizedType t = (ParameterizedType) typeArg;
            Class rawType = (Class) t.getRawType();
            if (rawType != java.util.List.class)
                throw new RuntimeException("Can only handle lists");
            final Type[] typeArguments = t.getActualTypeArguments();
            if (typeArguments.length != 1)
                throw new RuntimeException("Can only handle one type argument");
            return (Class) typeArguments[0];
        }
        return null;
    }
}
