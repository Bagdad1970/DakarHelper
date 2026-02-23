package io.github.bagdad.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductResponse {

    private String name;

    private String vendorTitle;

    private BigDecimal price;

    private Integer totalQuantity;

    private BigDecimal priceWithMargin;

}
