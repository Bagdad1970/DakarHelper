package io.github.bagdad.dakarhelperservice.service.interfaces;

import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.models.emailhandler.VendorWithMaxFileDateTime;

import java.util.List;
import java.util.Optional;

public interface VendorService {

    Vendor create(Vendor vendor);

    void batchInsert(List<Vendor> vendors);

    Vendor update(Vendor vendor);

    List<Vendor> findAll();

    Optional<Vendor> findById(Long id);

    void deleteById(Long id);

    List<VendorWithMaxFileDateTime> findVendorsWithLastFileDate();

}
