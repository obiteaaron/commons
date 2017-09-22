package com.github.obiteaaron.commons.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Properties;
import java.util.Set;

/**
 * 用于替换placeholder的工具类,依赖于spring,仅在spring项目下使用,代码来源于{@link PropertyPlaceholderConfigurer#parseStringValue(String, Properties, Set)}
 *
 * @author Obite Aaron
 * @since 1.0
 */
public class SpringPlaceholderHelper {


    /**
     * Default placeholder prefix: {@value}
     */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /**
     * Default placeholder suffix: {@value}
     */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    /**
     * Default value separator: {@value}
     */
    public static final String DEFAULT_VALUE_SEPARATOR = ":";


    /**
     * Defaults to {@value #DEFAULT_PLACEHOLDER_PREFIX}
     */
    protected static String placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;

    /**
     * Defaults to {@value #DEFAULT_PLACEHOLDER_SUFFIX}
     */
    protected static String placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;

    /**
     * Defaults to {@value #DEFAULT_VALUE_SEPARATOR}
     */
    protected static String valueSeparator = DEFAULT_VALUE_SEPARATOR;

    protected static boolean ignoreUnresolvablePlaceholders = false;


    public static String resolve(String strVal, Properties props) {
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(
                placeholderPrefix, placeholderSuffix, valueSeparator, ignoreUnresolvablePlaceholders);
        PropertyPlaceholderHelper.PlaceholderResolver resolver = new PropertyPlaceholderConfigurerResolver(props);
        return helper.replacePlaceholders(strVal, resolver);
    }

    private static class PropertyPlaceholderConfigurerResolver implements PropertyPlaceholderHelper.PlaceholderResolver {

        private final Properties props;

        private PropertyPlaceholderConfigurerResolver(Properties props) {
            this.props = props;
        }

        public String resolvePlaceholder(String placeholderName) {
            return props.getProperty(placeholderName);
        }
    }
}
