package io.github.bagdad.dakarhelperservice.repository.interfaces;

import io.github.bagdad.dakarhelperservice.model.ExcelHeaderCell;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderCellWithSubcategory;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderSubcategory;
import io.github.bagdad.models.excelparser.Category;

import java.util.List;
import java.util.Optional;

public interface ExcelHeaderCellRepository {

    ExcelHeaderCell save(ExcelHeaderCell excelHeaderCell);

    void batchInsert(List<ExcelHeaderCell> excelHeaderCells);

    ExcelHeaderCell update(ExcelHeaderCell excelHeaderCell);

    List<ExcelHeaderCell> findAll();

    Optional<ExcelHeaderCell> findById(Long id);

    void delete(Long id);

    List<ExcelHeaderCellWithSubcategory> findAllWithSubcategory();

    List<ExcelHeaderCell> findAllByCategory(Category category);

}
