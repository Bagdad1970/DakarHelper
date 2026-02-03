package io.github.bagdad.dakarhelperservice.service;

import io.github.bagdad.dakarhelperservice.model.FileStatus;
import io.github.bagdad.dakarhelperservice.model.VendorFile;
import io.github.bagdad.dakarhelperservice.repository.implementation.VendorFileRepositoryImpl;
import io.github.bagdad.dakarhelperservice.service.implementation.VendorFileServiceImpl;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class VendorFileServiceTest {

    @Mock
    private VendorFileRepositoryImpl repository;

    @InjectMocks
    private VendorFileServiceImpl service;

    @Test
    void findAll() {
        // arrange
        OffsetDateTime now = OffsetDateTime.now();

        VendorFile vendorFile1 = VendorFile.builder()
                .id(1L)
                .vendorId(1L)
                .filepath("filepath1")
                .fileStatus(FileStatus.CREATED)
                .updatedAt(now)
                .build();

        VendorFile vendorFile2 = VendorFile.builder()
                .id(2L)
                .vendorId(2L)
                .filepath("filepath2")
                .fileStatus(FileStatus.CREATED)
                .updatedAt(now)
                .build();

        List<VendorFile> vendorFiles = List.of(vendorFile1, vendorFile2);

        Mockito.when(repository.findAll())
                .thenReturn(vendorFiles);

        // act
        List<VendorFile> found = service.findAll();

        // assert
        assertThat(found).isNotNull();
        assertThat(found).hasSize(2);
        assertThat(found).isEqualTo(vendorFiles);
    }

    @Test
    void update() {
        // arrange
        OffsetDateTime now = OffsetDateTime.now();

        VendorFile existingVendorFile = VendorFile.builder()
                .id(1L)
                .vendorId(1L)
                .filepath("filepath1")
                .fileStatus(FileStatus.CREATED)
                .updatedAt(now)
                .build();

        VendorFile updatedVendorFile = VendorFile.builder()
                .id(1L)
                .vendorId(2L)
                .filepath("filepath2")
                .fileStatus(FileStatus.PARSED)
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(existingVendorFile));
        Mockito.when(repository.update(existingVendorFile))
                .thenReturn(existingVendorFile);

        // act
        VendorFile result = service.update(updatedVendorFile);

        // assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull().isEqualTo(updatedVendorFile.getId());
        assertThat(result.getVendorId()).isNotNull().isEqualTo(updatedVendorFile.getVendorId());
        assertThat(result.getFilepath()).isNotNull().isEqualTo(updatedVendorFile.getFilepath());
        assertThat(result.getFileStatus()).isNotNull().isEqualTo(updatedVendorFile.getFileStatus());
        assertThat(result.getUpdatedAt()).isNotNull();
        Mockito.verify(repository, times(1))
                .findById(1L);
        Mockito.verify(repository, times(1))
                .update(existingVendorFile);
    }

    @Test
    void batchInsert() {
        // arrange
        VendorFile vendorFile1 = VendorFile.builder()
                .vendorId(1L)
                .filepath("filepath1")
                .fileStatus(FileStatus.CREATED)
                .build();

        VendorFile vendorFile2 = VendorFile.builder()
                .vendorId(2L)
                .filepath("filepath2")
                .fileStatus(FileStatus.CREATED)
                .build();

        List<VendorFile> vendorFiles = List.of(vendorFile1, vendorFile2);

        // act
        service.batchInsert(vendorFiles);

        // assert
        Mockito.verify(repository, times(1))
                .batchInsert(vendorFiles);
    }

}
