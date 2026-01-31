package io.github.bagdad.dakarhelperservice.controller;

import io.github.bagdad.dakarhelperservice.model.ExcelHeaderSubcategory;
import io.github.bagdad.dakarhelperservice.service.interfaces.ExcelHeaderSubcategoryService;
import io.github.bagdad.models.request.excelheadersubcategory.ExcelHeaderSubcategoryCreateRequest;
import io.github.bagdad.models.request.excelheadersubcategory.ExcelHeaderSubcategoryDeleteRequest;
import io.github.bagdad.models.request.excelheadersubcategory.ExcelHeaderSubcategoryUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/excel-header-subcategory")
@RestController
public class ExcelHeaderSubcategoryController {

    private final ExcelHeaderSubcategoryService service;

    public ExcelHeaderSubcategoryController(ExcelHeaderSubcategoryService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ExcelHeaderSubcategory create(@RequestBody ExcelHeaderSubcategoryCreateRequest request) {
        ExcelHeaderSubcategory excelHeaderSubcategory = new ExcelHeaderSubcategory();

        excelHeaderSubcategory.setSubcategoryName(request.getSubcategoryName());

        return service.create(excelHeaderSubcategory);
    }

    @GetMapping("/all")
    public List<ExcelHeaderSubcategory> findAll() {
        return service.findAll();
    }

    @PutMapping("/update")
    public ExcelHeaderSubcategory update(@RequestBody ExcelHeaderSubcategoryUpdateRequest request) {
        ExcelHeaderSubcategory excelHeaderSubcategory = new ExcelHeaderSubcategory();

        excelHeaderSubcategory.setId(request.getId());
        excelHeaderSubcategory.setSubcategoryName(request.getSubcategoryName());

        return service.update(excelHeaderSubcategory);
    }

    @DeleteMapping("/delete")
    public void deleteById(@RequestBody ExcelHeaderSubcategoryDeleteRequest request) {
        service.deleteById(request.getId());
    }

}