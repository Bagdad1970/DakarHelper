package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.exception.VendorNotFoundException;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.repository.implementation.VendorRepositoryImpl;
import io.github.bagdad.dakarhelperservice.service.interfaces.VendorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepositoryImpl repository;

    public VendorServiceImpl(VendorRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public Vendor create(Vendor vendor) {
        return repository.save(vendor);
    }

    @Override
    public Vendor update(Vendor vendor) {
        Vendor existing = repository.findById(vendor.getId())
                .orElseThrow(() -> new VendorNotFoundException(vendor.getId()));

        if (vendor.getTitle() != null) existing.setTitle(vendor.getTitle());

        return repository.update(existing);
    }

    @Override
    public List<Vendor> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

}
