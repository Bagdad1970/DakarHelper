package io.github.bagdad.dakarhelperservice.repository.interfaces;

import io.github.bagdad.dakarhelperservice.model.Storage;

import java.util.List;
import java.util.Optional;

public interface StorageRepository {

    void saveAll(List<Storage> storages);

    Storage save(Storage storage);

    List<Storage> findAll();

    Optional<Storage> findById(String id);

    void deleteById(String id);

    void deleteByVendorFileId(Long id);
}