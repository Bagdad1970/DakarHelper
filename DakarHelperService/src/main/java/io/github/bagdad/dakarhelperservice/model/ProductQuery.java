package io.github.bagdad.dakarhelperservice.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuery {

    private List<Long> vendorIds;

    private String name;

    private BigDecimal price;

    private List<String> priceSubcategoryIds;

    private Integer quantity;

    private List<String> quantitySubcategoryIds;

}
