package io.github.bagdad.dakarhelperservice.model;

import io.github.bagdad.models.excelparser.Category;
import lombok.*;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class HeaderCell {

    @Id
    private Long id;

    private Long subcategoryId;

    private String originalName;

    private Category category;

    private Boolean isProcessing;

}
