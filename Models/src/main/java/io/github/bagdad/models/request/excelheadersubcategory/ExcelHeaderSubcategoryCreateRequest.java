package io.github.bagdad.models.request.excelheadersubcategory;

import io.github.bagdad.models.excelparser.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExcelHeaderSubcategoryCreateRequest {

    private String subcategoryName;

}
