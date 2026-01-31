package io.github.bagdad.dakarhelperservice.controller;

import io.github.bagdad.dakarhelperservice.model.ExcelProduct;
import io.github.bagdad.dakarhelperservice.model.ExcelProductQuery;
import io.github.bagdad.dakarhelperservice.service.interfaces.ExcelProductService;
import io.github.bagdad.models.request.excelproduct.ExcelProductQueryRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/excel-products")
public class ExcelProductController {

    private final ExcelProductService service;

    public ExcelProductController(ExcelProductService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<ExcelProduct> findAll() {
        return service.findAll();
    }

    @PostMapping("/query")
    public List<ExcelProduct> query(@RequestBody ExcelProductQueryRequest request) {
        ExcelProductQuery query = new ExcelProductQuery();


        return service.query(query);
    }

}
