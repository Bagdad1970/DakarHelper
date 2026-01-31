package io.github.bagdad.dakarhelperservice.service.interfaces;

import io.github.bagdad.dakarhelperservice.model.ExcelHeaderCell;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderCellWithSubcategory;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderSubcategory;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.models.excelparser.Category;

import java.util.List;

public interface ExcelHeaderCellService {

    ExcelHeaderCell create(ExcelHeaderCell excelHeaderCell);

    ExcelHeaderCell update(ExcelHeaderCell excelHeaderCell);

    void batchInsert(List<ExcelHeaderCell> excelHeaderCells);

    List<ExcelHeaderCell> findAll();

    void delete(Long id);

    List<ExcelHeaderCellWithSubcategory> findAllWithSubcategory();

    List<ExcelHeaderCell> findAllByCategory(Category category);

}
