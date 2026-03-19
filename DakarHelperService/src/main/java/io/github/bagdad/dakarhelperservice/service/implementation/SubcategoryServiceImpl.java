package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.exception.SubcategoryNotFoundException;
import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.dakarhelperservice.repository.interfaces.SubcategoryRepository;
import io.github.bagdad.dakarhelperservice.service.interfaces.SubcategoryService;
import io.github.bagdad.models.excelparser.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {

    private final SubcategoryRepository repository;

    public SubcategoryServiceImpl(SubcategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Subcategory create(Subcategory subcategory) {
        return repository.save(subcategory);
    }

    @Override
    public Subcategory update(Subcategory subcategory) {
        Subcategory existing = repository.findById(subcategory.getId())
                .orElseThrow(() -> new SubcategoryNotFoundException(subcategory.getId()));

        if (subcategory.getCategory() != null) existing.setCategory(subcategory.getCategory());
        if (subcategory.getName() != null) existing.setName(subcategory.getName());

        return repository.update(existing);
    }

    @Override
    public Optional<Subcategory> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Subcategory> findAll() {
        return repository.findAll();
    }

    @Override
    public int deleteById(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public List<Subcategory> findAllByCategory(Category category) {
        return repository.findAllByCategory(category);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        repository.batchDelete(ids);
    }

}
