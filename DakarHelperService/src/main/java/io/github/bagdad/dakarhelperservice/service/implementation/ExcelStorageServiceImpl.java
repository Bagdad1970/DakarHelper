package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.model.ExcelStorage;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ExcelStorageRepository;
import io.github.bagdad.dakarhelperservice.service.interfaces.ExcelStorageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExcelStorageServiceImpl implements ExcelStorageService {

    private final ExcelStorageRepository repository;

    public ExcelStorageServiceImpl(ExcelStorageRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(ExcelStorage excelStorage) {
        repository.save(excelStorage);
    }

    @Override
    public List<ExcelStorage> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ExcelStorage> findById(Long id) {
        return repository.findById(id);
    }

}
