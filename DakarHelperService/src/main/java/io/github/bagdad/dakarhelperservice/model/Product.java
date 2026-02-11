package io.github.bagdad.dakarhelperservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Map;

@Document(collection = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {

    @Id
    @EqualsAndHashCode.Exclude
    private String id;

    @Field(name="vendor_file_id")
    @Indexed(unique = false)
    @EqualsAndHashCode.Exclude
    private Long vendorFileId;

    @Field(name="name")
    private String name;

    @Field(name="prices")
    private Map<String, BigDecimal> prices;

    @Field(name="min_price")
    private BigDecimal minPrice;

    @Field(name="quantities")
    private Map<String, Integer> quantities;

    @Field(name="total_quantity")
    private Integer totalQuantity;

}
