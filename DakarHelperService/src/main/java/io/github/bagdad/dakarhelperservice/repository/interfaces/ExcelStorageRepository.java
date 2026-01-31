package io.github.bagdad.dakarhelperservice.repository.interfaces;

import io.github.bagdad.dakarhelperservice.model.ExcelStorage;

import java.util.List;
import java.util.Optional;

public interface ExcelStorageRepository {

    void saveAll(List<ExcelStorage> excelStorages);

    void save(ExcelStorage excelStorage);

    List<ExcelStorage> findAll();

    Optional<ExcelStorage> findById(Long id);

}
