package io.github.bagdad.dakarhelperservice.service.interfaces;

import io.github.bagdad.dakarhelperservice.model.FileStatus;
import io.github.bagdad.dakarhelperservice.model.VendorFile;

import java.util.List;

public interface VendorFileService {

    VendorFile update(VendorFile vendorFile);

    void batchInsert(List<VendorFile> vendorFiles);

    void batchUpdate(List<VendorFile> vendorFiles);

    List<VendorFile> findAll();

    List<VendorFile> findByFileStatus(FileStatus fileStatus);

    List<VendorFile> findByVendorId(Long id);

    void deleteByVendorId(Long id);

}
