package io.github.bagdad.dakarhelperservice.repository.interfaces;

import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface ProductRepository {

    void saveAll(Collection<Product> products);

    List<Product> findAll();

    Page<Product> query(ProductQuery query, Pageable pageable);

    void deleteByVendorId(Long id);

}
