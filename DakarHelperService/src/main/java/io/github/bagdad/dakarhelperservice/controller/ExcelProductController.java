package io.github.bagdad.dakarhelperservice.controller;

import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.dakarhelperservice.service.interfaces.ProductService;
import io.github.bagdad.models.request.excelproduct.ExcelProductQueryRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/excel-products")
public class ExcelProductController {

    private final ProductService service;

    public ExcelProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Product> findAll() {
        return service.findAll();
    }

    @PostMapping("/query")
    public List<Product> query(@RequestBody ExcelProductQueryRequest request) {
        ProductQuery query = new ProductQuery();

        query.setVendorIds(request.getVendorIds());
        query.setName(request.getName());
        query.setPrice(request.getPrice());
        query.setPriceSubcategoryIds(request.getPriceSubcategoryIds());
        query.setQuantity(request.getQuantity());
        query.setQuantitySubcategoryIds(request.getQuantitySubcategoryIds());

        return service.query(query);
    }

}
