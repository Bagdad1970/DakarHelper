package io.github.bagdad.excelparser.utils;

import java.util.*;

public class SubcategoryMapping {

    private final Map<String, List<String>> mapping;

    public SubcategoryMapping(Map<String, List<String>> mapping) {
        this.mapping = mapping;
    }

    public String getKeyByValue(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        for (String subcategory : mapping.keySet()) {
            for (String originName : mapping.get(subcategory)) {
                if (value.contains(originName))
                    return subcategory;
            }
        }
        return null;
    }

}