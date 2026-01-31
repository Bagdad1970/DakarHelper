package io.github.bagdad.models.request.excelheadercell;

import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelHeaderCellUpdateRequest {

    private Long id;

    private Long excelHeaderSubcategoryId;

    private String originName;

    private String normalizedName;

    private Category category;

    private CellStatus cellStatus;

}
