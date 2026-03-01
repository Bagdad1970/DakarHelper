package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.exception.HeaderCellNotFoundException;
import io.github.bagdad.dakarhelperservice.model.HeaderCellWithSubcategory;
import io.github.bagdad.dakarhelperservice.repository.interfaces.HeaderCellRepository;
import io.github.bagdad.dakarhelperservice.service.interfaces.HeaderCellService;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HeaderCellServiceImpl implements HeaderCellService {

    private final HeaderCellRepository repository;

    public HeaderCellServiceImpl(HeaderCellRepository repository) {
        this.repository = repository;
    }

    @Override
    public HeaderCell create(HeaderCell headerCell) {
        return repository.save(headerCell);
    }

    @Override
    public HeaderCell update(HeaderCell headerCell) {
        HeaderCell existing = repository.findById(headerCell.getId())
                .orElseThrow(() -> new HeaderCellNotFoundException(headerCell.getId()));

        if (headerCell.getSubcategoryId() != null) existing.setSubcategoryId(headerCell.getSubcategoryId());
        if (headerCell.getOriginalName() != null) existing.setOriginalName(headerCell.getOriginalName());
        if (headerCell.getNormalizedName() != null) existing.setNormalizedName(headerCell.getNormalizedName());
        if (headerCell.getCategory() != null) existing.setCategory(headerCell.getCategory());
        if (headerCell.getCellStatus() != null) existing.setCellStatus(headerCell.getCellStatus());

        return repository.update(existing);
    }

    @Override
    public void batchInsert(List<HeaderCell> headerCells) {
        repository.batchInsert(headerCells);
    }

    @Override
    public List<HeaderCell> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<HeaderCell> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<HeaderCellWithSubcategory> findAllWithSubcategory() {
        return repository.findAllWithSubcategory();
    }

    @Override
    public List<HeaderCell> findAllByCategory(Category category) {
        return repository.findAllByCategory(category);
    }

}
