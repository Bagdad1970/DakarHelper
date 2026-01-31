package io.github.bagdad.dakarhelperservice.repository;

import io.github.bagdad.dakarhelperservice.DakarHelperTestConfiguration;
import io.github.bagdad.dakarhelperservice.exception.VendorFileNotFoundException;
import io.github.bagdad.dakarhelperservice.model.FileStatus;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.model.VendorFile;
import io.github.bagdad.dakarhelperservice.repository.implementation.VendorFileRepositoryImpl;
import io.github.bagdad.dakarhelperservice.repository.implementation.VendorRepositoryImpl;
import io.github.bagdad.dakarhelperservice.repository.interfaces.VendorFileRepository;
import io.github.bagdad.dakarhelperservice.repository.interfaces.VendorRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@JdbcTest
@ContextConfiguration(classes = DakarHelperTestConfiguration.class)
public class VendorFileRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private VendorFileRepository vendorFileRepository;
    private VendorRepository vendorRepository;

    @BeforeEach
    void setUp() {
        this.vendorFileRepository = new VendorFileRepositoryImpl(jdbcTemplate);
        this.vendorRepository = new VendorRepositoryImpl(jdbcTemplate);
    }

    private static Vendor createVendorForTesting() {
        Vendor vendor = new Vendor();
        vendor.setTitle("title");

        return vendor;
    }

    private static VendorFile createVendorFileForTesting(Vendor vendor) {
        OffsetDateTime now = OffsetDateTime.now();

        VendorFile vendorFile = new VendorFile();
        vendorFile.setVendorId(vendor.getId());
        vendorFile.setFilepath("filepath");
        vendorFile.setFileStatus(FileStatus.CREATED);
        vendorFile.setCreatedAt(now);
        vendorFile.setUpdatedAt(now);

        return vendorFile;
    }

    @Test
    void Saving_vendor_file_must_save_and_return_saved_entity() {
        Vendor vendor = createVendorForTesting();
        Vendor savedVendor = vendorRepository.save(vendor);

        VendorFile vendorFile = createVendorFileForTesting(savedVendor);
        VendorFile savedVendorFile = vendorFileRepository.save(vendorFile);

        Optional<VendorFile> found = vendorFileRepository.findById(savedVendorFile.getId());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(savedVendorFile);
    }

    @Test
    void Batch_inserting_vendor_files_must_save_all_with_one_operation() {
        Vendor vendor1 = createVendorForTesting();
        Vendor vendor2 = createVendorForTesting();
        Vendor vendor3 = createVendorForTesting();

        Vendor savedVendor1 = vendorRepository.save(vendor1);
        Vendor savedVendor2 = vendorRepository.save(vendor2);
        Vendor savedVendor3 = vendorRepository.save(vendor3);

        VendorFile vendorFile1 = createVendorFileForTesting(savedVendor1);
        VendorFile vendorFile2 = createVendorFileForTesting(savedVendor2);
        VendorFile vendorFile3 = createVendorFileForTesting(savedVendor3);

        List<VendorFile> vendorFiles = new ArrayList<>();
        vendorFiles.add(vendorFile1);
        vendorFiles.add(vendorFile2);
        vendorFiles.add(vendorFile3);

        vendorFileRepository.batchInsert(vendorFiles);

        List<VendorFile> saved = vendorFileRepository.findAll();
        assertThat(saved).hasSize(vendorFiles.size());
    }

    @Test
    void Finding_all_vendor_files_must_return_existing_entities() {
        Vendor vendor = createVendorForTesting();
        Vendor savedVendor = vendorRepository.save(vendor);

        VendorFile vendorFile = createVendorFileForTesting(savedVendor);
        vendorFileRepository.save(vendorFile);

        List<VendorFile> files = vendorFileRepository.findAll();

        assertThat(files).isNotEmpty();
        assertThat(files).allMatch(
                f -> f.getId() != null &&
                f.getFilepath() != null &&
                f.getVendorId() != null &&
                f.getFileStatus() != null &&
                f.getCreatedAt() != null &&
                f.getUpdatedAt() != null
        );
    }

    @Test
    void Finding_existing_vendor_file_by_id_must_return_existing_entity() {
        Vendor vendor = createVendorForTesting();
        Vendor savedVendor = vendorRepository.save(vendor);

        VendorFile vendorFile = createVendorFileForTesting(savedVendor);
        VendorFile savedVendorFile = vendorFileRepository.save(vendorFile);

        Optional<VendorFile> found = vendorFileRepository.findById(savedVendorFile.getId());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(savedVendorFile);
    }

    @Test
    void Finding_non_existing_vendor_file_by_id_must_return_empty() {
        Optional<VendorFile> found = vendorFileRepository.findById(0L);

        assertThat(found).isEmpty();
    }

    @Test
    void Updating_existing_vendor_file_must_update_and_return_updated_entity() {
        Vendor vendor1 = createVendorForTesting();
        Vendor savedVendor1 = vendorRepository.save(vendor1);

        Vendor vendor2 = createVendorForTesting();
        Vendor savedVendor2 = vendorRepository.save(vendor2);

        VendorFile vendorFile = createVendorFileForTesting(savedVendor1);
        VendorFile savedVendorFile = vendorFileRepository.save(vendorFile);

        savedVendorFile.setVendorId(savedVendor2.getId());
        savedVendorFile.setFilepath("updated_filepath");
        VendorFile updatedVendorFile = vendorFileRepository.update(savedVendorFile);

        Optional<VendorFile> found = vendorFileRepository.findById(updatedVendorFile.getId());
        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(updatedVendorFile);
    }

    @Test
    void Updating_non_existing_vendor_file_must_throw_exception() {
        VendorFile nonExistent = new VendorFile();
        nonExistent.setId(0L);
        nonExistent.setFileStatus(FileStatus.CREATED);

        assertThatThrownBy(() -> vendorFileRepository.update(nonExistent))
                .isInstanceOf(VendorFileNotFoundException.class);
    }

}