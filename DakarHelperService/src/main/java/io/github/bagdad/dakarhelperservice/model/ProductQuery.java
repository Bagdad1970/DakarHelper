package io.github.bagdad.dakarhelperservice.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductQuery {

    private List<Long> vendorIds;

    private String name;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal margin;

    private Integer pageIndex;

    private Integer pageSize;

}
