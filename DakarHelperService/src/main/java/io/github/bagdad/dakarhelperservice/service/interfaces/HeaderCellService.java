package io.github.bagdad.dakarhelperservice.service.interfaces;

import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderCellWithSubcategory;
import io.github.bagdad.models.excelparser.Category;

import java.util.List;

public interface HeaderCellService {

    HeaderCell create(HeaderCell headerCell);

    HeaderCell update(HeaderCell headerCell);

    void batchInsert(List<HeaderCell> headerCells);

    List<HeaderCell> findAll();

    void delete(Long id);

    List<ExcelHeaderCellWithSubcategory> findAllWithSubcategory();

    List<HeaderCell> findAllByCategory(Category category);

}
