package io.github.bagdad.dakarhelperservice.model;

import io.github.bagdad.models.excelparser.Category;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("excel_header_subcategories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Subcategory {

    @Id
    private Long id;

    private String name;

    private Category category;

}
