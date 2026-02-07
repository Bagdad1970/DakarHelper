package io.github.bagdad.dakarhelperservice.controller;

import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.service.interfaces.VendorService;
import io.github.bagdad.models.request.vendor.VendorDeleteRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import io.github.bagdad.models.request.vendor.VendorCreateRequest;
import io.github.bagdad.models.request.vendor.VendorUpdateRequest;

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
        Vendor vendor = new Vendor();

        vendor.setTitle(request.getTitle());

        return service.create(vendor);
    }

    @GetMapping
    public List<Vendor> findAll() {
        return service.findAll();
    }

    @PutMapping
    public Vendor update(@RequestBody VendorUpdateRequest request) {
        Vendor vendor = new Vendor();

        vendor.setId(request.getId());
        vendor.setTitle(request.getTitle());

        return service.update(vendor);
    }

    @DeleteMapping
    public void delete(@RequestBody VendorDeleteRequest request) {
        Long id = request.getId();

        service.delete(id);
    }

}
