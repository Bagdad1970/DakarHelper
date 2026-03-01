package io.github.bagdad.dakarhelperservice.service.interfaces;

import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.models.excelparser.Category;

import java.util.List;
import java.util.Optional;

public interface SubcategoryService {

    Subcategory create(Subcategory subcategory);

    Subcategory update(Subcategory subcategory);

    Optional<Subcategory> findById(Long id);

    List<Subcategory> findAll();

    void deleteById(Long id);

    List<Subcategory> findAllByCategory(Category category);

}
