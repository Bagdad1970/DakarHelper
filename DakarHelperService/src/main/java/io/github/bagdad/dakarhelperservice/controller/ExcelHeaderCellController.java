package io.github.bagdad.dakarhelperservice.controller;

import io.github.bagdad.dakarhelperservice.model.ExcelHeaderCell;
import io.github.bagdad.dakarhelperservice.service.interfaces.ExcelHeaderCellService;
import io.github.bagdad.models.request.excelheadercell.ExcelHeaderCellCreateRequest;
import io.github.bagdad.models.request.excelheadercell.ExcelHeaderCellDeleteRequest;
import io.github.bagdad.models.request.excelheadercell.ExcelHeaderCellUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/excel-header-cell")
public class ExcelHeaderCellController {

    private final ExcelHeaderCellService service;

    public ExcelHeaderCellController(ExcelHeaderCellService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ExcelHeaderCell create(@RequestBody ExcelHeaderCellCreateRequest request) {
        ExcelHeaderCell excelHeaderCell = new ExcelHeaderCell();

        excelHeaderCell.setExcelHeaderSubcategoryId(request.getExcelHeaderSubcategoryId());
        excelHeaderCell.setOriginName(request.getOriginName());
        excelHeaderCell.setNormalizedName(request.getNormalizedName());
        excelHeaderCell.setCategory(request.getCategory());
        excelHeaderCell.setCellStatus(request.getCellStatus());

        return service.create(excelHeaderCell);
    }

    @PutMapping("/update")
    public ExcelHeaderCell update(@RequestBody ExcelHeaderCellUpdateRequest request) {
        ExcelHeaderCell excelHeaderCell = new ExcelHeaderCell();

        excelHeaderCell.setId(request.getId());
        excelHeaderCell.setExcelHeaderSubcategoryId(request.getExcelHeaderSubcategoryId());
        excelHeaderCell.setOriginName(request.getOriginName());
        excelHeaderCell.setNormalizedName(request.getNormalizedName());
        excelHeaderCell.setCategory(request.getCategory());
        excelHeaderCell.setCellStatus(request.getCellStatus());

        return service.update(excelHeaderCell);
    }

    @GetMapping("/all")
    public List<ExcelHeaderCell> findAll() {
        return service.findAll();
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody ExcelHeaderCellDeleteRequest request) {
        Long id = request.getId();

        service.delete(id);
    }

}
