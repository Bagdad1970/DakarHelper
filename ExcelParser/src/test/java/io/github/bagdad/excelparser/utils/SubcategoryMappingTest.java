package io.github.bagdad.excelparser.utils;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SubcategoryMappingTest {

    @Test
    void getKeyByAliasWithEmptyValue() {
        Map<String, List<String>> mapping = Map.of(
                "опт", List.of("оптовый")
        );
        SubcategoryMapping subcategoryMapping = new SubcategoryMapping(mapping);

        String key1 = subcategoryMapping.getKeyByValue(null);
        String key2 = subcategoryMapping.getKeyByValue("оптовый");

        assertNull(key1);
        assertEquals("опт", key2);
    }


}
