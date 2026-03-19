package io.github.bagdad.dakarhelperservice.controller;

import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.model.HeaderCellWithSubcategory;
import io.github.bagdad.dakarhelperservice.service.interfaces.HeaderCellService;
import io.github.bagdad.models.request.BatchDeleteRequest;
import io.github.bagdad.models.request.headercell.HeaderCellCreateRequest;
import io.github.bagdad.models.request.headercell.HeaderCellUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/header-cells")
public class HeaderCellController {

    private final HeaderCellService service;

    public HeaderCellController(HeaderCellService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<HeaderCell> create(@RequestBody HeaderCellCreateRequest request) {
        HeaderCell headerCell = HeaderCell.builder()
                .subcategoryId(request.getSubcategoryId())
                .originalName(request.getOriginalName())
                .category(request.getCategory())
                .isProcessing(request.getIsProcessing())
                .build();

        HeaderCell created = service.create(headerCell);

        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HeaderCell> update(@PathVariable Long id, @RequestBody HeaderCellUpdateRequest request) {
        HeaderCell headerCell = HeaderCell.builder()
                .id(request.getId())
                .subcategoryId(request.getSubcategoryId())
                .originalName(request.getOriginalName())
                .category(request.getCategory())
                .isProcessing(request.getIsProcessing())
                .build();

        HeaderCell updated = service.update(headerCell);

        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeaderCell> findById(@PathVariable Long id) {
        HeaderCell headerCell = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "HeaderCell not found with id: " + id
                ));

        return ResponseEntity.ok(headerCell);
    }

    @GetMapping
    public List<HeaderCell> findAll() {
        return service.findAll();
    }

    @GetMapping("/with-subcategory")
    public List<HeaderCellWithSubcategory> findAllWithSubcategory() {
        return service.findAllWithSubcategory();
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
