package io.github.bagdad.dakarhelperservice.service.interfaces;

import io.github.bagdad.dakarhelperservice.model.Storage;

import java.util.List;
import java.util.Optional;

public interface StorageService {

    Storage save(Storage storage);

    List<Storage> findAll();

    Optional<Storage> findById(String id);

    void deleteById(String id);

}
