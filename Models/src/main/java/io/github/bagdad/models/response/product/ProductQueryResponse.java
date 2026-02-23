package io.github.bagdad.models.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductQueryResponse {

    private List<ProductQueryItem> productData;

    private Pagination pagination;

}
