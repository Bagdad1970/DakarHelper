package io.github.bagdad.dakarhelperservice.repository.interfaces;

import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.models.emailhandler.VendorWithMaxFileDateTime;

import java.util.List;
import java.util.Optional;

public interface VendorRepository {

    void batchInsert(List<Vendor> vendors);

    Vendor save(Vendor Vendor);

    Vendor update(Vendor Vendor);

    void deleteById(Long id);

    List<Vendor> findAll();

    Optional<Vendor> findById(Long id);

    List<VendorWithMaxFileDateTime> findVendorsWithLastFileTimestamp();
    
}
