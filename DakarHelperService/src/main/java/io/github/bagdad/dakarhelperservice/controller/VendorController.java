package io.github.bagdad.dakarhelperservice.controller;

import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.service.interfaces.VendorService;
import io.github.bagdad.models.request.vendor.VendorDeleteRequest;
import org.springframework.web.bind.annotation.*;
import io.github.bagdad.models.request.vendor.VendorCreateRequest;
import io.github.bagdad.models.request.vendor.VendorUpdateRequest;

import java.util.List;

@RequestMapping("/api/vendor")
@RestController
public class VendorController {

    private final VendorService service;

    public VendorController(VendorService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Vendor create(@RequestBody VendorCreateRequest request) {
        Vendor vendor = new Vendor();

        vendor.setTitle(request.getTitle());

        return service.create(vendor);
    }

    @GetMapping("/all")
    public List<Vendor> findAll() {
        return service.findAll();
    }

    @PutMapping("/update")
    public Vendor update(@RequestBody VendorUpdateRequest request) {
        Vendor vendor = new Vendor();

        vendor.setId(request.getId());
        vendor.setTitle(request.getTitle());

        return service.update(vendor);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody VendorDeleteRequest request) {
        Long id = request.getId();

        service.delete(id);
    }

}
