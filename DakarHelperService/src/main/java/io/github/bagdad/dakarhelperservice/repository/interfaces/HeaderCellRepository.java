package io.github.bagdad.dakarhelperservice.repository.interfaces;

import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.model.HeaderCellWithSubcategory;
import io.github.bagdad.models.excelparser.Category;

import java.util.List;
import java.util.Optional;

public interface HeaderCellRepository {

    HeaderCell save(HeaderCell headerCell);

    void batchInsert(List<HeaderCell> headerCells);

    HeaderCell update(HeaderCell headerCell);

    List<HeaderCell> findAll();

    Optional<HeaderCell> findById(Long id);

    void deleteById(Long id);

    List<HeaderCellWithSubcategory> findAllWithSubcategory();

    List<HeaderCell> findAllByCategory(Category category);

}
