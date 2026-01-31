package io.github.bagdad.dakarhelperservice.repository;

import io.github.bagdad.dakarhelperservice.DakarHelperTestConfiguration;
import io.github.bagdad.dakarhelperservice.exception.VendorNotFoundException;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.repository.implementation.VendorRepositoryImpl;
import io.github.bagdad.dakarhelperservice.repository.interfaces.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@JdbcTest
@ContextConfiguration(classes = DakarHelperTestConfiguration.class)
class VendorRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private VendorRepository repository;

    @BeforeEach
    void setUp() {
        this.repository = new VendorRepositoryImpl(jdbcTemplate);
    }

    private static Vendor createVendorForTesting() {
        Vendor vendor = new Vendor();
        vendor.setTitle("title");

        return vendor;
    }

    @Test
    void Saving_vendor_must_save_and_return_saved_vendor() {
        Vendor vendor = new Vendor();
        vendor.setTitle("save_title");

        Vendor savedEntity = repository.save(vendor);

        Optional<Vendor> foundEntity = repository.findById(savedEntity.getId());

        assertThat(foundEntity).isPresent();
        assertThat(foundEntity.get()).isEqualTo(savedEntity);
    }

    @Test
    void Batch_inserting_vendors_must_save_all_vendors_with_one_operation() {
        Vendor vendor1 = createVendorForTesting();
        Vendor vendor2 = createVendorForTesting();
        Vendor vendor3 = createVendorForTesting();

        List<Vendor> vendors = new ArrayList<>();
        vendors.add(vendor1);
        vendors.add(vendor2);
        vendors.add(vendor3);

        repository.batchInsert(vendors);

        List<Vendor> saved = repository.findAll();
        assertThat(saved).hasSize(vendors.size());
        assertThat(saved).allMatch(v -> v.getId() != null && v.getTitle() != null);
    }

    @Test
    void Finding_existing_vendor_by_id_must_return_existing_vendor() {
        Vendor vendor = createVendorForTesting();

        Vendor savedVendor = repository.save(vendor);

        Optional<Vendor> foundedVendor = repository.findById(savedVendor.getId());

        assertThat(foundedVendor).isPresent();
        assertThat(foundedVendor.get()).isEqualTo(savedVendor);
    }

    @Test
    void Finding_non_existing_vendor_by_id_must_return_empty() {
        Optional<Vendor> vendor = repository.findById(0L);

        assertThat(vendor).isEmpty();
    }

    @Test
    void Updating_vendor_must_update_and_return_this_vendor() {
        Vendor vendor = createVendorForTesting();

        Vendor saved = repository.save(vendor);

        saved.setTitle("updated_title");
        Vendor updated = repository.update(saved);

        Optional<Vendor> founded = repository.findById(updated.getId());

        assertThat(founded).isPresent();
        assertThat(founded.get()).isEqualTo(updated);
    }

    @Test
    void Updating_non_existing_vendor_must_throw_exception() {
        Vendor vendor = new Vendor();
        vendor.setId(0L);

        assertThatThrownBy(() -> repository.update(vendor))
                .isInstanceOf(VendorNotFoundException.class);
    }

    @Test
    void Deleting_existing_vendor_must_remove_it() {
        Vendor vendor = createVendorForTesting();

        Vendor saved = repository.save(vendor);

        repository.delete(saved.getId());

        Optional<Vendor> deleted = repository.findById(saved.getId());
        assertThat(deleted).isEmpty();
    }

    @Test
    void Deleting_non_existing_vendor_must_throw_exception() {
        Long id = 0L;

        assertThatThrownBy(() -> repository.delete(id))
                .isInstanceOf(VendorNotFoundException.class);
    }
}