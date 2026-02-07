package io.github.bagdad.dakarhelperservice.repository.interfaces;

import io.github.bagdad.dakarhelperservice.model.FileStatus;
import io.github.bagdad.dakarhelperservice.model.VendorFile;

import java.util.List;
import java.util.Optional;

public interface VendorFileRepository {

    VendorFile save(VendorFile vendorFile);

    VendorFile update(VendorFile VendorFile);

    void batchInsert(List<VendorFile> VendorFiles);

    void batchUpdate(List<VendorFile> vendorFiles);

    List<VendorFile> findAll();

    List<VendorFile> findByFileStatus(FileStatus fileStatus);

    Optional<VendorFile> findById(Long id);

    void deleteByVendorId(Long id);

    List<VendorFile> findByVendorId(Long id);


}
