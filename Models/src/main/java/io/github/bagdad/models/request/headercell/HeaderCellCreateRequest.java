package io.github.bagdad.models.request.headercell;

import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeaderCellCreateRequest {

    private Long subcategoryId;

    @NotBlank(message = "Origin name cannot be blank")
    private String originalName;

    @NotBlank(message = "Normalized name cannot be blank")
    private String normalizedName;

    @NotBlank(message = "Category cannot be blank")
    private Category category;

    private CellStatus cellStatus;

}
