package io.github.bagdad.models.request.subcategory;

import io.github.bagdad.models.excelparser.Category;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubcategoryUpdateRequest {

    private Category category;

    private String name;

}
