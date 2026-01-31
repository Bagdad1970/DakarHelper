package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.exception.ExcelHeaderSubcategoryNotFoundException;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderSubcategory;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ExcelHeaderSubcategoryRepository;
import io.github.bagdad.dakarhelperservice.service.interfaces.ExcelHeaderSubcategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExcelHeaderSubcategoryServiceImpl implements ExcelHeaderSubcategoryService {

    private final ExcelHeaderSubcategoryRepository repository;

    public ExcelHeaderSubcategoryServiceImpl(ExcelHeaderSubcategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public ExcelHeaderSubcategory create(ExcelHeaderSubcategory excelHeaderSubcategory) {
        return repository.save(excelHeaderSubcategory);
    }

    @Override
    public ExcelHeaderSubcategory update(ExcelHeaderSubcategory excelHeaderSubcategory) {
        ExcelHeaderSubcategory existing = repository.findById(excelHeaderSubcategory.getId())
                .orElseThrow(() -> new ExcelHeaderSubcategoryNotFoundException(excelHeaderSubcategory.getId()));

        if (excelHeaderSubcategory.getSubcategoryName() != null) existing.setSubcategoryName(excelHeaderSubcategory.getSubcategoryName());

        return repository.update(existing);
    }

    @Override
    public Optional<ExcelHeaderSubcategory> findById(ExcelHeaderSubcategory excelHeaderSubcategory) {
        Long id = excelHeaderSubcategory.getId();

        return repository.findById(id);
    }

    @Override
    public List<ExcelHeaderSubcategory> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repository.delete(id);
    }


}
