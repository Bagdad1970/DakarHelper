package io.github.bagdad.dakarhelperservice.service.interfaces;

import io.github.bagdad.dakarhelperservice.model.Vendor;

import java.util.List;

public interface VendorService {

    Vendor create(Vendor vendor);

    Vendor update(Vendor vendor);

    List<Vendor> findAll();

    void delete(Long id);
}
