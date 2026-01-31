package io.github.bagdad.dakarhelperservice.model;

import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("excel_header_cells")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ExcelHeaderCell {

    @Id
    private Long id;

    private Long excelHeaderSubcategoryId;

    private String originName;

    private String normalizedName;

    private Category category;

    private CellStatus cellStatus;

}
