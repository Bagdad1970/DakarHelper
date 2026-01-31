package io.github.bagdad.models.request.excelheadersubcategory;

import io.github.bagdad.models.excelparser.Category;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExcelHeaderSubcategoryUpdateRequest {

    private Long id;

    private String subcategoryName;

}
