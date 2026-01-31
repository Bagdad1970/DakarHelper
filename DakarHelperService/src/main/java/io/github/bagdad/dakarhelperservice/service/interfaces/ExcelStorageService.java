package io.github.bagdad.dakarhelperservice.service.interfaces;

import io.github.bagdad.dakarhelperservice.model.ExcelStorage;

import java.util.List;
import java.util.Optional;

public interface ExcelStorageService {

    void save(ExcelStorage excelStorage);

    List<ExcelStorage> findAll();

    Optional<ExcelStorage> findById(Long id);

}
