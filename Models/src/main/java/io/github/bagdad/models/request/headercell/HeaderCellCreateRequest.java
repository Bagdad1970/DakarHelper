package io.github.bagdad.models.request.headercell;

import io.github.bagdad.models.excelparser.Category;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeaderCellCreateRequest {

    private Long subcategoryId;

    @NotBlank(message = "Original name cannot be blank")
    private String originalName;

    private Category category;

    private Boolean isProcessing;

    @AssertTrue(message = "Either subcategoryId or category must be provided")
    private boolean isValidCategoryOrSubcategory() {
        return subcategoryId != null || category != null;
    }

}
