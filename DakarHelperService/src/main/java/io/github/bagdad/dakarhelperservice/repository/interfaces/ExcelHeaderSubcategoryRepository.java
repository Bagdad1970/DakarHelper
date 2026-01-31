package io.github.bagdad.dakarhelperservice.repository.interfaces;

import io.github.bagdad.dakarhelperservice.model.ExcelHeaderSubcategory;

import java.util.List;
import java.util.Optional;

public interface ExcelHeaderSubcategoryRepository {

    ExcelHeaderSubcategory save(ExcelHeaderSubcategory excelHeaderSubcategory);

    ExcelHeaderSubcategory update(ExcelHeaderSubcategory excelHeaderSubcategory);

    List<ExcelHeaderSubcategory> findAll();

    void delete(Long id);

    Optional<ExcelHeaderSubcategory> findById(Long id);

}
