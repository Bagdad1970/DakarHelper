package io.github.bagdad.models.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductQueryItem {

    private String name;

    private String vendorTitle;

    private BigDecimal minPrice;

    private Integer totalQuantity;

    private BigDecimal priceWithMargin;

}