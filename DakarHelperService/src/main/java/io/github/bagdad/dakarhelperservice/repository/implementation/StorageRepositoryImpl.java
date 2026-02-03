package io.github.bagdad.dakarhelperservice.repository.implementation;

import io.github.bagdad.dakarhelperservice.model.Storage;
import io.github.bagdad.dakarhelperservice.repository.interfaces.StorageRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StorageRepositoryImpl implements StorageRepository {

    private final MongoTemplate mongoTemplate;

    public StorageRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveAll(List<Storage> excelProducts) {
        mongoTemplate.insertAll(excelProducts);
    }

    @Override
    public Storage save(Storage storage) {
        return mongoTemplate.save(storage);
    }

    @Override
    public List<Storage> findAll() {
        return mongoTemplate.findAll(Storage.class);
    }

    @Override
    public void deleteById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Storage.class);
    }

    @Override
    public Optional<Storage> findById(String id) {
        try {
            Storage storage = mongoTemplate.findById(id, Storage.class);
            return Optional.ofNullable(storage);
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
