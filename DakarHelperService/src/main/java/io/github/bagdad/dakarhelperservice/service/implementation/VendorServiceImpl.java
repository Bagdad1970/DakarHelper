package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.exception.VendorNotFoundException;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.repository.interfaces.VendorRepository;
import io.github.bagdad.dakarhelperservice.service.interfaces.VendorService;
import io.github.bagdad.models.emailhandler.VendorWithMaxFileDateTime;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository repository;

    public VendorServiceImpl(VendorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Vendor create(Vendor vendor) {
        return repository.save(vendor);
    }

    @Override
    public void batchInsert(List<Vendor> vendors) {
        repository.batchInsert(vendors);
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
    public Optional<Vendor> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public int deleteById(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public List<VendorWithMaxFileDateTime> findVendorsWithLastFileDate() {
        return repository.findVendorsWithLastFileTimestamp();
    }

    @Override
    public void batchDelete(List<Long> ids) {
        repository.batchDelete(ids);
    }

}
