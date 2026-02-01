package io.github.bagdad.dakarhelperservice.controller;

import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.dakarhelperservice.service.interfaces.SubcategoryService;
import io.github.bagdad.models.request.subcategory.SubcategoryCreateRequest;
import io.github.bagdad.models.request.subcategory.SubcategoryDeleteRequest;
import io.github.bagdad.models.request.subcategory.SubcategoryUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/excel-header-subcategory")
@RestController
public class SubcategoryController {

    private final SubcategoryService service;

    public SubcategoryController(SubcategoryService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Subcategory create(@RequestBody SubcategoryCreateRequest request) {
        Subcategory subcategory = new Subcategory();

        subcategory.setCategory(request.getCategory());
        subcategory.setName(request.getName());

        return service.create(subcategory);
    }

    @GetMapping("/all")
    public List<Subcategory> findAll() {
        return service.findAll();
    }

    @PutMapping("/update")
    public Subcategory update(@RequestBody SubcategoryUpdateRequest request) {
        Subcategory subcategory = new Subcategory();

        subcategory.setId(request.getId());
        subcategory.setCategory(request.getCategory());
        subcategory.setName(request.getName());

        return service.update(subcategory);
    }

    @DeleteMapping("/delete")
    public void deleteById(@RequestBody SubcategoryDeleteRequest request) {
        service.deleteById(request.getId());
    }

}