package io.github.bagdad.dakarhelperservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("excel_header_subcategories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ExcelHeaderSubcategory {

    @Id
    private Long id;

    private String subcategoryName;

}
