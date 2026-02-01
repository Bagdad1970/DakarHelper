package io.github.bagdad.models.request.headercell;

import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeaderCellUpdateRequest {

    private Long id;

    private Long subcategoryId;

    private String originalName;

    private String normalizedName;

    private Category category;

    private CellStatus cellStatus;

}
