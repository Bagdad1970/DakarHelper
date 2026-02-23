package io.github.bagdad.dakarhelperservice.service.interfaces;

import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.models.response.ProductResponse;

import java.util.Collection;
import java.util.List;

public interface ProductService {

    void saveAll(Collection<Product> products);

    List<ProductResponse> query(ProductQuery query);

    void deleteByVendorId(Long id);

}
