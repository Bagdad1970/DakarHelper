package io.github.bagdad.dakarhelperservice.service.interfaces;

import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.model.HeaderCellWithSubcategory;
import io.github.bagdad.models.excelparser.Category;

import java.util.List;
import java.util.Optional;

public interface HeaderCellService {

    HeaderCell create(HeaderCell headerCell);

    HeaderCell update(HeaderCell headerCell);

    void batchInsert(List<HeaderCell> headerCells);

    List<HeaderCell> findAll();

    Optional<HeaderCell> findById(Long id);

    void deleteById(Long id);

    List<HeaderCellWithSubcategory> findAllWithSubcategory();

    List<HeaderCell> findAllByCategory(Category category);

}
