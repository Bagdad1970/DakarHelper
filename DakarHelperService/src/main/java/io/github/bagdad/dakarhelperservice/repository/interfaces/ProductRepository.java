package io.github.bagdad.dakarhelperservice.repository.interfaces;

import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;

import java.util.Collection;
import java.util.List;

public interface ProductRepository {

    void saveAll(Collection<Product> products);

    List<Product> query(ProductQuery query);

    void deleteByVendorId(Long id);

}
