package io.github.bagdad.dakarhelperservice.model;

import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExcelHeaderCellWithSubcategory {

    private Long id;

    private Long excelHeaderSubcategoryId;

    private Long subcategoryId;

    private Category category;

    private String subcategoryName;

    private String originName;

    private String normalizedName;

    private CellStatus cellStatus;

}
