package io.github.bagdad.models.request.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductQueryRequest {


    private List<Long> vendorIds;

    private String name;

    @Positive(message = "Quantity must be positive")
    private BigDecimal price;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Min(value = 0, message = "Margin cannot be negative")
    private BigDecimal margin = BigDecimal.valueOf(0);

}
