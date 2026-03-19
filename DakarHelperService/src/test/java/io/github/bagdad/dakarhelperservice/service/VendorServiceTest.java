package io.github.bagdad.dakarhelperservice.service;

import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.repository.implementation.VendorRepositoryImpl;
import io.github.bagdad.dakarhelperservice.service.implementation.VendorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class VendorServiceTest {

    @Mock
    private VendorRepositoryImpl repository;

    @InjectMocks
    private VendorServiceImpl service;

    @Test
    void findAll() {
        Vendor vendor1 = Vendor.builder()
                .id(1L)
                .title("title1")
                .build();

        Vendor vendor2 = Vendor.builder()
                .id(1L)
                .title("title2")
                .build();

        List<Vendor> vendors = List.of(vendor1, vendor2);

        Mockito.when(repository.findAll())
                .thenReturn(vendors);

        List<Vendor> found = service.findAll();

        assertThat(found).isNotNull();
        assertThat(found).hasSize(2);
        assertThat(found).isEqualTo(vendors);
    }

    @Test
    void findById() {
        Vendor vendor = Vendor.builder()
                .id(1L)
                .title("title1")
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(vendor));

        Optional<Vendor> found = service.findById(1L);

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(vendor);
    }

    @Test
    void create() {
        // arrange
        Vendor vendor = Vendor.builder()
                .title("title")
                .build();

        Vendor savedVendor = Vendor.builder()
                .id(1L)
                .title("title")
                .build();

        Mockito.when(repository.save(vendor)).thenReturn(savedVendor);

        // act
        Vendor result = service.create(vendor);

        // assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(savedVendor);
    }

    @Test
    void update() {
        // arrange
        Vendor existingVendor = Vendor.builder()
                .id(1L)
                .title("title")
                .build();

        Vendor updatedVendor = Vendor.builder()
                .id(1L)
                .title("updated_title")
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(existingVendor));
        Mockito.when(repository.update(existingVendor))
                .thenReturn(existingVendor);

        // act
        Vendor result = service.update(updatedVendor);

        // assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(updatedVendor);
        Mockito.verify(repository, times(1))
                .findById(1L);
        Mockito.verify(repository, times(1))
                .update(existingVendor);
    }

    @Test
    void batchInsert() {
        // arrange
        Vendor vendor1 = Vendor.builder()
                .title("title")
                .build();

        Vendor vendor2 = Vendor.builder()
                .title("updated_title")
                .build();

        List<Vendor> vendors = List.of(vendor1, vendor2);

        // act
        service.batchInsert(vendors);

        // assert
        Mockito.verify(repository, times(1))
                .batchInsert(vendors);
    }

    @Test
    void deleteById() {
        Mockito.when(repository.deleteById(1L))
                .thenReturn(1);
        
        int result = service.deleteById(1L);

        assertThat(result).isEqualTo(1);
        Mockito.verify(repository, times(1))
                .deleteById(1L);
    }

    @Test
    void batchDelete() {
        // arrange
        Vendor vendor1 = Vendor.builder()
                .title("title")
                .build();

        Vendor vendor2 = Vendor.builder()
                .title("updated_title")
                .build();

        List<Vendor> vendors = List.of(vendor1, vendor2);

        // act
        service.batchInsert(vendors);

        // assert
        Mockito.verify(repository, times(1))
                .batchInsert(vendors);
    }

}