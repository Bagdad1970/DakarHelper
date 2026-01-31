package io.github.bagdad.dakarhelperservice.repository.implementation;

import io.github.bagdad.dakarhelperservice.model.ExcelProduct;
import io.github.bagdad.dakarhelperservice.model.ExcelProductQuery;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ExcelProductRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExcelProductRepositoryImpl implements ExcelProductRepository {

    private final MongoTemplate mongoTemplate;

    public ExcelProductRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveAll(List<ExcelProduct> excelProducts) {
        mongoTemplate.insertAll(excelProducts);
    }

    @Override
    public List<ExcelProduct> findAll() {
        return mongoTemplate.findAll(ExcelProduct.class);
    }

    @Override
    public List<ExcelProduct> query(ExcelProductQuery query) {
        return List.of();
    }

}
