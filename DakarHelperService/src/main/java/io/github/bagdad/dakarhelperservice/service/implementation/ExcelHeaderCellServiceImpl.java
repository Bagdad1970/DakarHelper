package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.exception.ExcelHeaderCellNotFoundException;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderCellWithSubcategory;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderSubcategory;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ExcelHeaderCellRepository;
import io.github.bagdad.dakarhelperservice.service.interfaces.ExcelHeaderCellService;
import io.github.bagdad.dakarhelperservice.service.interfaces.ExcelHeaderSubcategoryService;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.ExcelHeaderCellDto;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderCell;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelHeaderCellServiceImpl implements ExcelHeaderCellService {

    private final ExcelHeaderCellRepository repository;

    public ExcelHeaderCellServiceImpl(ExcelHeaderCellRepository repository) {
        this.repository = repository;
    }

    @Override
    public ExcelHeaderCell create(ExcelHeaderCell excelHeaderCell) {
        return repository.save(excelHeaderCell);
    }

    @Override
    public ExcelHeaderCell update(ExcelHeaderCell excelHeaderCell) {
        ExcelHeaderCell existing = repository.findById(excelHeaderCell.getId())
                .orElseThrow(() -> new ExcelHeaderCellNotFoundException(excelHeaderCell.getId()));

        if (excelHeaderCell.getExcelHeaderSubcategoryId() != null) existing.setExcelHeaderSubcategoryId(excelHeaderCell.getExcelHeaderSubcategoryId());
        if (excelHeaderCell.getOriginName() != null) existing.setOriginName(excelHeaderCell.getOriginName());
        if (excelHeaderCell.getNormalizedName() != null) existing.setNormalizedName(excelHeaderCell.getNormalizedName());
        if (excelHeaderCell.getCategory() != null) existing.setCategory(excelHeaderCell.getCategory());
        if (excelHeaderCell.getCellStatus() != null) existing.setCellStatus(excelHeaderCell.getCellStatus());

        return repository.update(existing);
    }

    @Override
    public void batchInsert(List<ExcelHeaderCell> excelHeaderCells) {
        repository.batchInsert(excelHeaderCells);
    }

    @Override
    public List<ExcelHeaderCell> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public List<ExcelHeaderCellWithSubcategory> findAllWithSubcategory() {
        return repository.findAllWithSubcategory();
    }

    @Override
    public List<ExcelHeaderCell> findAllByCategory(Category category) {
        return repository.findAllByCategory(category);
    }

}
