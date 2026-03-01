package io.github.bagdad.dakarhelperservice.controller;

import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.service.interfaces.VendorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.github.bagdad.models.request.vendor.VendorCreateRequest;
import io.github.bagdad.models.request.vendor.VendorUpdateRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("/api/vendors")
@RestController
public class VendorController {

    private final VendorService service;

    public VendorController(VendorService service) {
        this.service = service;
    }

    @PostMapping
    public Vendor create(@Valid @RequestBody VendorCreateRequest request) {
        Vendor vendor = Vendor.builder()
                .title(request.getTitle())
                .build();

        return service.create(vendor);
    }

    @GetMapping
    public List<Vendor> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> findById(@PathVariable Long id) {
        Vendor vendor = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Subcategory not found with id: " + id
                ));

        return ResponseEntity.ok(vendor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendor> update(@PathVariable Long id, @Valid @RequestBody VendorUpdateRequest request) {
        Vendor vendor = Vendor.builder()
                .id(id)
                .title(request.getTitle())
                .build();

        Vendor updatedVendor = service.update(vendor);

        return ResponseEntity.ok(updatedVendor);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }

}
