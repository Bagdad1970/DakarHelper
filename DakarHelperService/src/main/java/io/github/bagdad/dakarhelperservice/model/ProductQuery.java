package io.github.bagdad.dakarhelperservice.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductQuery {

    private List<Long> vendorIds;

    private String name;

    private BigDecimal price;

    private Integer quantity;

}
