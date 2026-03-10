package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.exception.HeaderCellNotFoundException;
import io.github.bagdad.dakarhelperservice.exception.SubcategoryNotFoundException;
import io.github.bagdad.dakarhelperservice.model.HeaderCellWithSubcategory;
import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.dakarhelperservice.repository.interfaces.HeaderCellRepository;
import io.github.bagdad.dakarhelperservice.service.interfaces.HeaderCellService;
import io.github.bagdad.dakarhelperservice.service.interfaces.SubcategoryService;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HeaderCellServiceImpl implements HeaderCellService {

    private final HeaderCellRepository repository;
    private final SubcategoryService subcategoryService;

    public HeaderCellServiceImpl(HeaderCellRepository repository, SubcategoryService subcategoryService) {
        this.repository = repository;
        this.subcategoryService = subcategoryService;
    }

    @Override
    public HeaderCell create(HeaderCell headerCell) {
        Long subcategoryId = headerCell.getSubcategoryId();

        if (headerCell.getCategory() == null && subcategoryId != null) {
            Subcategory subcategory = subcategoryService.findById(headerCell.getSubcategoryId())
                    .orElseThrow(() -> new SubcategoryNotFoundException(subcategoryId));
            headerCell.setCategory(subcategory.getCategory());
        }

        return repository.save(headerCell);
    }

    @Override
    public HeaderCell update(HeaderCell headerCell) {
        HeaderCell existing = repository.findById(headerCell.getId())
                .orElseThrow(() -> new HeaderCellNotFoundException(headerCell.getId()));

        existing.setSubcategoryId(headerCell.getSubcategoryId());  // no check for null because it can be null
        if (headerCell.getOriginalName() != null) existing.setOriginalName(headerCell.getOriginalName());

        Long subcategoryId = headerCell.getSubcategoryId();
        if (headerCell.getCategory() == null) {
            Subcategory subcategory = subcategoryService.findById(subcategoryId)
                    .orElseThrow(() -> new SubcategoryNotFoundException(subcategoryId));
            existing.setCategory(subcategory.getCategory());
        }
        else {
            existing.setCategory(headerCell.getCategory());
        }

        if (headerCell.getIsProcessing() != null) existing.setIsProcessing(headerCell.getIsProcessing());

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
