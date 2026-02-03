package io.github.bagdad.excelparser.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Product {

    private Map<String, String> names = new HashMap<>();

    private Map<String, BigDecimal> prices = new HashMap<>();

    private Map<String, Integer> quantities = new HashMap<>();

    public void addName(String key, String name) {
        names.put(key, name);
    }

    public void addPrice(String key, BigDecimal price) {
        prices.put(key, price);
    }

    public void addQuantity(String key, Integer quantity) {
        quantities.put(key, quantity);
    }

}
