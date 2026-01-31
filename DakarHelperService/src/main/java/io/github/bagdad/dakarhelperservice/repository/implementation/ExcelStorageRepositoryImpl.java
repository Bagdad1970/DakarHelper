package io.github.bagdad.dakarhelperservice.repository.implementation;

import io.github.bagdad.dakarhelperservice.model.ExcelProduct;
import io.github.bagdad.dakarhelperservice.model.ExcelStorage;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ExcelStorageRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ExcelStorageRepositoryImpl implements ExcelStorageRepository {

    private final MongoTemplate mongoTemplate;

    public ExcelStorageRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveAll(List<ExcelStorage> excelProducts) {
        mongoTemplate.insertAll(excelProducts);
    }

    @Override
    public void save(ExcelStorage excelStorage) {
        mongoTemplate.save(excelStorage);
    }

    @Override
    public List<ExcelStorage> findAll() {
        return mongoTemplate.findAll(ExcelStorage.class);
    }

    @Override
    public Optional<ExcelStorage> findById(Long id) {
        try {
            ExcelStorage storage = mongoTemplate.findById(id, ExcelStorage.class);
            return Optional.ofNullable(storage);
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
