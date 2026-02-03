package io.github.bagdad.models.request.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQueryRequest {

    private List<Long> vendorIds;

    private String name;

    private BigDecimal price;

    private List<String> priceSubcategoryIds;

    private Integer quantity;

    private List<String> quantitySubcategoryIds;

}
