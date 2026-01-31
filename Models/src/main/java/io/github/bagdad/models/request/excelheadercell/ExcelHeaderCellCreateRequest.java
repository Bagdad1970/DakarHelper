package io.github.bagdad.models.request.excelheadercell;

import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelHeaderCellCreateRequest {

    private Long excelHeaderSubcategoryId;

    @NotBlank(message = "Origin name cannot be blank")
    private String originName;

    @NotBlank(message = "Normalized name cannot be blank")
    private String normalizedName;

    @NotBlank(message = "Category cannot be blank")
    private Category category;

    private CellStatus cellStatus;

}
