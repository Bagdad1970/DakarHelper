package io.github.bagdad.dakarhelperservice.repository;

import io.github.bagdad.dakarhelperservice.DakarHelperTestConfiguration;
import io.github.bagdad.dakarhelperservice.model.Storage;
import io.github.bagdad.dakarhelperservice.repository.implementation.StorageRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ContextConfiguration(classes = DakarHelperTestConfiguration.class)
@Import(StorageRepositoryImpl.class)
@Testcontainers
class StorageRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StorageRepositoryImpl repository;

    @AfterEach
    void cleanUp() {
        mongoTemplate.dropCollection(Storage.class);
    }

    private static Storage createExcelStorageForTesting() {
        Storage storage = new Storage();
        storage.setVendorFileId(1L);
        storage.setStorages(Map.of("storage", "storage"));

        return storage;
    }

    @Test
    void Saving_excel_storage_must_save_and_return_saved_excelStorage() {
        Storage storage = createExcelStorageForTesting();

        Storage savedEntity = repository.save(storage);

        Optional<Storage> found = repository.findById(savedEntity.getId());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(savedEntity);
    }

    @Test
    void Finding_existing_excel_storage_by_id_must_return_existing_excelStorage() {
        Storage storage = createExcelStorageForTesting();

        Storage savedStorage = repository.save(storage);

        Optional<Storage> foundedExcelStorage = repository.findById(savedStorage.getId());

        assertThat(foundedExcelStorage).isPresent();
        assertThat(foundedExcelStorage.get()).isEqualTo(savedStorage);
    }

    @Test
    void Finding_non_existing_excel_storage_by_id_must_return_empty() {
        Optional<Storage> excelStorage = repository.findById("0");

        assertThat(excelStorage).isEmpty();
    }

    @Test
    void Finding_all_excel_storages_must_return_existing_entities() {
        List<Storage> storages = new ArrayList<>();
        Storage storage1 = createExcelStorageForTesting();
        Storage storage2 = createExcelStorageForTesting();
        storages.add(storage1);
        storages.add(storage2);

        repository.saveAll(storages);

        List<Storage> found = repository.findAll();

        assertThat(found).isNotEmpty();
        assertThat(found).isEqualTo(storages);
    }

    @Test
    void Deleting_existing_excel_storage_must_delete_it() {
        Storage storage = createExcelStorageForTesting();

        Storage saved = repository.save(storage);

        repository.deleteById(saved.getId());

        Optional<Storage> deleted = repository.findById(saved.getId());
        assertThat(deleted).isEmpty();
    }

}
