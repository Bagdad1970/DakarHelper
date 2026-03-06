package io.github.bagdad.dakarhelperservice.controller;

import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.dakarhelperservice.service.interfaces.ProductService;
import io.github.bagdad.models.request.product.ProductQueryRequest;
import io.github.bagdad.models.response.product.ProductQueryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping("/query")
    public ResponseEntity<ProductQueryResponse> query(@RequestBody ProductQueryRequest request) {
        ProductQuery query = new ProductQuery();

        query.setVendorIds(request.getVendorIds());
        query.setName(request.getName());
        query.setPrice(request.getPrice());
        query.setQuantity(request.getQuantity());
        query.setMargin(BigDecimal.valueOf(request.getMargin() == null ? 0.0 : request.getMargin()));
        query.setPageIndex(request.getPageIndex());
        query.setPageSize(request.getPageSize());

        ProductQueryResponse response = service.query(query);

        return ResponseEntity.ok(response);
    }

}
