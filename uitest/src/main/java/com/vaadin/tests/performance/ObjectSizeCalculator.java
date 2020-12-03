package com.vaadin.tests.performance;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;

import com.vaadin.ui.AbstractComponent;

/**
 * Helper class for calculating a very rough estimate of a component's memory
 * consumption. Ignores all kinds of optimizations and any contents that cannot
 * be accessed via reflection, so the actual size is likely to be different.
 */
public class ObjectSizeCalculator {

    private static Instrumentation instrumentation;

    public static void setInstrumentation(Instrumentation instrumentatn) {
        instrumentation = instrumentatn;
    }

    public static long getObjectSize(AbstractComponent component) {
        return getObjectSize(component, new HashSet<Object>());
    }

    private static long getObjectSize(Object objectToSize,
            HashSet<Object> processed) {
        if (processed.contains(objectToSize)) {
            return 0;
        }
        processed.add(objectToSize);
        long result = getSize(objectToSize);

        Class<?> clazz = objectToSize.getClass();
        if (clazz.isArray()) {
            for (int i = 0; i < Array.getLength(objectToSize); ++i) {
                result += getObjectSize(Array.get(objectToSize, i), processed);
            }
            return result;
        }

        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {

                try {
                    fields[i].setAccessible(true);
                    result += getObjectSize(fields[i], processed);
                } catch (Throwable e) {
                    // NOP
                }
            }
            clazz = clazz.getSuperclass();
        }

        return result;
    }

    private static long getSize(Object objectToSize) {
        if (instrumentation == null) {
            return 0;
        }
        return instrumentation.getObjectSize(objectToSize);
    }
}
