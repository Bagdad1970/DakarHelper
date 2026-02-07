package io.github.bagdad.dakarhelperservice.service.interfaces;

import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;

import java.util.Collection;
import java.util.List;

public interface ProductService {

    void saveAll(Collection<Product> products);

    List<Product> findAll();

    List<Product> query(ProductQuery query);

    void deleteByVendorFileId(Long id);

}
