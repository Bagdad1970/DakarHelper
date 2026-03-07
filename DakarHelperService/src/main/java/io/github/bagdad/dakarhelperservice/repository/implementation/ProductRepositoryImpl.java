package io.github.bagdad.dakarhelperservice.repository.implementation;

import io.github.bagdad.dakarhelperservice.helper.ProductHelper;
import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

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

    @Override
    public Page<Product> query(ProductQuery productQuery, Pageable pageable) {
        if (productQuery.getVendorIds() == null || productQuery.getVendorIds().isEmpty()) {
            return Page.empty(pageable);
        }

        Query query = new Query();

        List<Criteria> fieldConditions = ProductHelper.createFieldConditions(productQuery);
        if (!fieldConditions.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(fieldConditions));
        }

        List<Sort.Order> sortingConditions = ProductHelper.createSortingConditions(productQuery);
        if (!sortingConditions.isEmpty()) {
            query.with(Sort.by(sortingConditions));
        }

        long total = mongoTemplate.count(query, Product.class);

        query.with(pageable);
        List<Product> products = mongoTemplate.find(query, Product.class);

        return PageableExecutionUtils.getPage(products, pageable, () -> total);
    }

    @Override
    public void deleteByVendorId(Long vendorId) {
        if (vendorId == null) {
            return;
        }

        Query query = Query.query(where("vendor_id").is(vendorId));
        mongoTemplate.remove(query, Product.class);
    }

}
