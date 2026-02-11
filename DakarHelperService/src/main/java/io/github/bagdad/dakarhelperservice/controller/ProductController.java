package io.github.bagdad.dakarhelperservice.controller;

import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.dakarhelperservice.service.interfaces.ProductService;
import io.github.bagdad.models.request.product.ProductQueryRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping("/query")
    public List<Product> query(@RequestBody ProductQueryRequest request) {
        ProductQuery query = new ProductQuery();

        query.setVendorIds(request.getVendorIds());
        query.setName(request.getName());
        query.setPrice(request.getPrice());
        query.setQuantity(request.getQuantity());

        return service.query(query);
    }

}
