package com.github.obiteaaron.commons.util;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 简单的placeholder替换
 *
 * @author Obite Aaron
 * @since 1.0
 */
public class SimplePlaceholderHelper {

    public static String resolve(String placeholder, Properties properties) {
        List<String> listKey = new LinkedList<>();
        List<String> listValue = new LinkedList<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            listKey.add("${" + entry.getKey() + "}");
            listValue.add(String.valueOf(entry.getValue()));
        }
        return StringUtils.replaceEach(placeholder, listKey.toArray(new String[0]), listValue.toArray(new String[0]));
    }
}
