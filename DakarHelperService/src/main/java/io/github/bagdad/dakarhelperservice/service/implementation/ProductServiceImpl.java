package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ProductRepository;
import io.github.bagdad.dakarhelperservice.service.interfaces.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveAll(List<Product> products) {
        repository.saveAll(products);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Product> query(ProductQuery query) {
        return repository.query(query);
    }

    @Override
    public void deleteByVendorFileId(Long id) {
        repository.deleteByVendorFileId(id);
    }

}
