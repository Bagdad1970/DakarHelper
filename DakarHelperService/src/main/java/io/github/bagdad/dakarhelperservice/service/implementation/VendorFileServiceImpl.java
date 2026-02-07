package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.exception.VendorFileNotFoundException;
import io.github.bagdad.dakarhelperservice.model.FileStatus;
import io.github.bagdad.dakarhelperservice.model.VendorFile;
import io.github.bagdad.dakarhelperservice.repository.interfaces.VendorFileRepository;
import io.github.bagdad.dakarhelperservice.service.interfaces.VendorFileService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class VendorFileServiceImpl implements VendorFileService {

    private final VendorFileRepository repository;

    public VendorFileServiceImpl(VendorFileRepository repository) {
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

            vendorFile.setUpdatedAt(now);
        }

        repository.batchInsert(vendorFiles);
    }

    @Override
    public void batchUpdate(List<VendorFile> vendorFiles) {

    }

    @Override
    public List<VendorFile> findAll() {
        return repository.findAll();
    }

    @Override
    public List<VendorFile> findByFileStatus(FileStatus fileStatus) {
        return repository.findByFileStatus(fileStatus);
    }

    @Override
    public List<VendorFile> findByVendorId(Long id) {
        return repository.findByVendorId(id);
    }

    @Override
    public void deleteByVendorId(Long id) {
        repository.deleteByVendorId(id);
    }

}
