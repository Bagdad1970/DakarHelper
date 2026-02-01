package io.github.bagdad.models.request.subcategory;

import io.github.bagdad.models.excelparser.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubcategoryCreateRequest {

    private Category category;

    private String name;

}
