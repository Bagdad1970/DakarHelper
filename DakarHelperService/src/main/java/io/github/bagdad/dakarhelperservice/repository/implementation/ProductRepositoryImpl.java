package io.github.bagdad.dakarhelperservice.repository.implementation;

import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ProductRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final MongoTemplate mongoTemplate;

    public ProductRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveAll(Collection<Product> products) {
        mongoTemplate.insertAll(products);
    }

    @Override
    public List<Product> findAll() {
        return mongoTemplate.findAll(Product.class);
    }

    public List<Product> query(ProductQuery query) {
        return List.of();
    }

    @Override
    public void deleteByVendorFileId(Long vendorFileId) {
        if (vendorFileId == null) {
            return;
        }
        Query query = Query.query(Criteria.where("vendor_file_id").is(vendorFileId));
        mongoTemplate.remove(query, Product.class);
    }

}
