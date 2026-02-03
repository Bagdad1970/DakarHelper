package io.github.bagdad.dakarhelperservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class VendorFile {

    @Id
    private Long id;

    private Long vendorId;

    private String filepath;

    private FileStatus fileStatus;

    private OffsetDateTime updatedAt;

}
