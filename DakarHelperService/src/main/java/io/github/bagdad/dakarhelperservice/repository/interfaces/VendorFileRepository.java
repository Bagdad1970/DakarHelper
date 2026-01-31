package io.github.bagdad.dakarhelperservice.repository.interfaces;

import io.github.bagdad.dakarhelperservice.model.VendorFile;

import java.util.List;
import java.util.Optional;

public interface VendorFileRepository {

    VendorFile save(VendorFile vendorFile);

    VendorFile update(VendorFile VendorFile);

    void batchInsert(List<VendorFile> VendorFiles);

    List<VendorFile> findAll();

    Optional<VendorFile> findById(Long id);

}
