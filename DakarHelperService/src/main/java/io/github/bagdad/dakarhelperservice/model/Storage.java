package io.github.bagdad.dakarhelperservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Document(collection = "storages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Storage {

    @Id
    private String id;

    @Field(name="vendor_file_id")
    private Long vendorFileId;

    @Field(name="storages")
    private Map<String, String> storages;

}