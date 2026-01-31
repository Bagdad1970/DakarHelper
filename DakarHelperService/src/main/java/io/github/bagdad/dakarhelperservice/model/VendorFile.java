package io.github.bagdad.dakarhelperservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Table("vendor_files")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class VendorFile {

    @Id
    private Long id;

    private Long vendorId;

    private String filepath;

    private FileStatus fileStatus;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
