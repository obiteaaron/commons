package com.github.obiteaaron.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 代码来自于 com.caucho.hessian.io.UnsafeSerializer
 *
 * @author Obite Aaron
 * @since 1.0
 */
public class UnsafeHolder {

    private static final Logger log
            = LoggerFactory.getLogger(UnsafeHolder.class.getName());

    private static final Unsafe _unsafe;

    private static final boolean _isEnabled;

    public static boolean isEnabled() {
        return _isEnabled;
    }

    public static Unsafe getUnsafe() {
        return _unsafe;
    }

    static {
        boolean isEnabled = false;
        Unsafe unsafe = null;

        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field theUnsafe = null;
            for (Field field : unsafeClass.getDeclaredFields()) {
                if (field.getName().equals("theUnsafe"))
                    theUnsafe = field;
            }

            if (theUnsafe != null) {
                theUnsafe.setAccessible(true);
                unsafe = (Unsafe) theUnsafe.get(null);
            }

            isEnabled = unsafe != null;

            String unsafeProp = System.getProperty("com.github.obiteaaron.unsafe");

            if ("false".equals(unsafeProp))
                isEnabled = false;
        } catch (Throwable e) {
            log.error(e.toString(), e);
        }

        _unsafe = unsafe;
        _isEnabled = isEnabled;
    }
}
