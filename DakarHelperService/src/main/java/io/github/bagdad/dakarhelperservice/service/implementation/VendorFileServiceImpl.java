package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.exception.VendorFileNotFoundException;
import io.github.bagdad.dakarhelperservice.model.FileStatus;
import io.github.bagdad.dakarhelperservice.model.VendorFile;
import io.github.bagdad.dakarhelperservice.repository.implementation.VendorFileRepositoryImpl;
import io.github.bagdad.dakarhelperservice.service.interfaces.VendorFileService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class VendorFileServiceImpl implements VendorFileService {

    private final VendorFileRepositoryImpl repository;

    public VendorFileServiceImpl(VendorFileRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public VendorFile update(VendorFile vendorFile) {
        VendorFile existing = repository.findById(vendorFile.getId())
                .orElseThrow(() -> new VendorFileNotFoundException(vendorFile.getId()));

        if (vendorFile.getVendorId() != null) existing.setVendorId(vendorFile.getVendorId());
        if (vendorFile.getFilepath() != null) existing.setFilepath(vendorFile.getFilepath());
        if (vendorFile.getFileStatus() != null) existing.setFileStatus(vendorFile.getFileStatus());

        OffsetDateTime now = OffsetDateTime.now();
        vendorFile.setUpdatedAt(now);

        return repository.update(existing);
    }

    @Override
    public void batchInsert(List<VendorFile> vendorFiles) {
        OffsetDateTime now = OffsetDateTime.now();

        for (VendorFile vendorFile : vendorFiles) {
            vendorFile.setFileStatus(FileStatus.CREATED);

            vendorFile.setCreatedAt(now);
            vendorFile.setUpdatedAt(now);
        }

        repository.batchInsert(vendorFiles);
    }

    @Override
    public List<VendorFile> findAll() {
        return repository.findAll();
    }

}
