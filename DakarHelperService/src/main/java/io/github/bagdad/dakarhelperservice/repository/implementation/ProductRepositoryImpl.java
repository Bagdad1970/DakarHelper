package io.github.bagdad.dakarhelperservice.repository.implementation;

import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ProductRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

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
    public List<Product> query(ProductQuery productQuery) {
        Query query = new Query();

        if (productQuery.getVendorIds() != null && !productQuery.getVendorIds().isEmpty()) {
            query.addCriteria(where("vendor_id").in(productQuery.getVendorIds()));
        }

        if (productQuery.getName() != null) {
            query.addCriteria(where("name").regex(Pattern.compile(productQuery.getName(), Pattern.CASE_INSENSITIVE)));
        }

        if (productQuery.getPrice() != null) {
            query.addCriteria(where("min_price").lte(productQuery.getPrice()));
        }

        if (productQuery.getQuantity() != null) {
            query.addCriteria(where("total_quantity").gte(productQuery.getQuantity()));
        }

        return mongoTemplate.query(Product.class)
                .matching(query)
                .all();
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
