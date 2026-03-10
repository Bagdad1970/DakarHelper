package io.github.bagdad.dakarhelperservice.model;

import io.github.bagdad.models.excelparser.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeaderCellWithSubcategory {

    private Long id;

    private String subcategoryName;

    private Category category;

    private String originalName;

    private Boolean isProcessing;

}
