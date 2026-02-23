package io.github.bagdad.dakarhelperservice.service.interfaces;

import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.models.response.product.ProductQueryResponse;

import java.util.Collection;

public interface ProductService {

    void saveAll(Collection<Product> products);

    ProductQueryResponse query(ProductQuery query);

    void deleteByVendorId(Long id);

}
