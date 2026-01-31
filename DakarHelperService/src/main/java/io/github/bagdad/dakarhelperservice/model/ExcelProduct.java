package io.github.bagdad.dakarhelperservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Map;

@Document(collection = "products")
@Getter
@Setter
public class ExcelProduct {

    @Id
    private String id;

    @Field(name="vendor_file_id")
    private Long vendorFileId;

    @Field(name="names")
    private Map<String, String> names;

    @Field(name="prices")
    private Map<String, BigDecimal> prices;

    @Field(name="quantities")
    private Map<String, Integer> quantities;

}
