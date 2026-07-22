package io.github.bagdad.excelparser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExcelProduct {

    private String article;

    private String name;

    private Map<String, BigDecimal> prices = new HashMap<>();

    private BigDecimal minPrice;

    private Map<String, Integer> quantities = new HashMap<>();

    private Integer totalQuantity;

    public void addPrice(String key, BigDecimal price) {
        prices.put(key, price);
    }

    public void computeMinPrice() {
        this.minPrice = prices.values().stream()
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.valueOf(Double.MAX_VALUE));
    }

    public void computeTotalQuantity() {
        this.totalQuantity = quantities.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public void addQuantity(String key, Integer quantity) {
        quantities.put(key, quantity);
    }

    public boolean isEmpty() {
        return name == null || prices.isEmpty() || quantities.isEmpty();
    }

}
