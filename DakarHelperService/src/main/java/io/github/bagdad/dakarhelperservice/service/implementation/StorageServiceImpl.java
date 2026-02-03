package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.model.Storage;
import io.github.bagdad.dakarhelperservice.repository.interfaces.StorageRepository;
import io.github.bagdad.dakarhelperservice.service.interfaces.StorageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StorageServiceImpl implements StorageService {

    private final StorageRepository repository;

    public StorageServiceImpl(StorageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Storage save(Storage storage) {
        return repository.save(storage);
    }

    @Override
    public List<Storage> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Storage> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

}
