package io.github.bagdad.dakarhelperservice.controller;

import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.dakarhelperservice.service.interfaces.SubcategoryService;
import io.github.bagdad.models.request.BatchDeleteRequest;
import io.github.bagdad.models.request.subcategory.SubcategoryCreateRequest;
import io.github.bagdad.models.request.subcategory.SubcategoryUpdateRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("/api/subcategories")
@RestController
public class SubcategoryController {

    private final SubcategoryService service;

    public SubcategoryController(SubcategoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<@NonNull Subcategory> create(@RequestBody SubcategoryCreateRequest request) {
        Subcategory subcategory = Subcategory.builder()
                .category(request.getCategory())
                .name(request.getName())
                .build();

        Subcategory created = service.create(subcategory);

        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull Subcategory> findById(@PathVariable Long id) {
        Subcategory subcategory = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Subcategory not found with id: " + id
                ));

        return ResponseEntity.ok(subcategory);
    }

    @GetMapping
    public List<Subcategory> findAll() {
        return service.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NonNull Subcategory> update(@PathVariable Long id, @RequestBody SubcategoryUpdateRequest request) {
        Subcategory subcategory = Subcategory.builder()
                .id(id)
                .category(request.getCategory())
                .name(request.getName())
                .build();

        Subcategory updated = service.update(subcategory);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }

    @PostMapping("/batch-delete")
    public void batchDelete(@Valid @RequestBody BatchDeleteRequest request) {
        service.batchDelete(request.getIds());
    }

}