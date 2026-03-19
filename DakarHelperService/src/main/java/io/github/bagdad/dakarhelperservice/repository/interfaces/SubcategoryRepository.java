package io.github.bagdad.dakarhelperservice.repository.interfaces;

import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.models.excelparser.Category;

import java.util.List;
import java.util.Optional;

public interface SubcategoryRepository {

    Subcategory save(Subcategory subcategory);

    Subcategory update(Subcategory subcategory);

    List<Subcategory> findAll();

    int deleteById(Long id);

    Optional<Subcategory> findById(Long id);

    List<Subcategory> findAllByCategory(Category category);

    void batchDelete(List<Long> ids);

}
