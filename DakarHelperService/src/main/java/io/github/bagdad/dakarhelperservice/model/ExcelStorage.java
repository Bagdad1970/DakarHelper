package io.github.bagdad.dakarhelperservice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Document(collection = "storages")
@Getter
@Setter
@EqualsAndHashCode
public class ExcelStorage {

    @Id
    private String id;

    @Field(name="vendor_file_id")
    private Long vendorFileId;

    @Field(name="storages")
    private Map<String, String> storages;

}