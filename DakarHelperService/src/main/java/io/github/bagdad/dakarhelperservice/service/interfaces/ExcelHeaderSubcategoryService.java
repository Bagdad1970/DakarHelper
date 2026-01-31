package io.github.bagdad.dakarhelperservice.service.interfaces;

import io.github.bagdad.dakarhelperservice.model.ExcelHeaderSubcategory;
import io.github.bagdad.models.excelparser.Category;

import java.util.List;
import java.util.Optional;

public interface ExcelHeaderSubcategoryService {

    ExcelHeaderSubcategory create(ExcelHeaderSubcategory excelHeaderSubcategory);

    ExcelHeaderSubcategory update(ExcelHeaderSubcategory excelHeaderSubcategory);

    Optional<ExcelHeaderSubcategory> findById(ExcelHeaderSubcategory excelHeaderSubcategory);

    List<ExcelHeaderSubcategory> findAll();

    void deleteById(Long id);

}
