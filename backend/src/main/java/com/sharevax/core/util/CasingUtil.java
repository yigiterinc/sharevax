package com.sharevax.core.util;

import lombok.experimental.UtilityClass;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class CasingUtil {

    public static String toCamelCase(String text) {
        String bactrianCamel = Stream.of(text.split("[^a-zA-Z0-9]"))
                .map(v -> v.substring(0, 1).toUpperCase() + v.substring(1).toLowerCase())
                .collect(Collectors.joining());

        return bactrianCamel;
    }
}
