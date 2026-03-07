package io.github.bagdad.dakarhelperservice.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductQuery {

    private List<Long> vendorIds;

    private String name;

    private BigDecimal minPrice;

    private Integer quantity;

    private BigDecimal margin;

    private LinkedHashMap<String, Integer> sortingConditions;

    private Integer pageIndex;

    private Integer pageSize;

}
