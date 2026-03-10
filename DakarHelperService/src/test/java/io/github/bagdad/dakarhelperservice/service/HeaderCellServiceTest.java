package io.github.bagdad.dakarhelperservice.service;

import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.dakarhelperservice.repository.implementation.HeaderCellRepositoryImpl;
import io.github.bagdad.dakarhelperservice.service.implementation.HeaderCellServiceImpl;
import io.github.bagdad.dakarhelperservice.service.implementation.SubcategoryServiceImpl;
import io.github.bagdad.dakarhelperservice.service.interfaces.SubcategoryService;
import io.github.bagdad.models.excelparser.Category;
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
public class HeaderCellServiceTest {

    @Mock
    private HeaderCellRepositoryImpl repository;

    @Mock private SubcategoryServiceImpl subcategoryService;

    @InjectMocks
    private HeaderCellServiceImpl service;

    @Test
    void Finding_all_header_cells_must_return_header_cells() {
        HeaderCell headerCell1 = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("original_name1")
                .category(Category.NAME)
                .isProcessing(true)
                .build();

        HeaderCell headerCell2 = HeaderCell.builder()
                .id(2L)
                .subcategoryId(2L)
                .originalName("original_name2")
                .category(Category.PRICE)
                .isProcessing(false)
                .build();

        List<HeaderCell> headerCells = List.of(headerCell1, headerCell2);

        Mockito.when(repository.findAll())
                .thenReturn(headerCells);

        List<HeaderCell> found = service.findAll();

        assertThat(found).isNotNull();
        assertThat(found).hasSize(2);
        assertThat(found).isEqualTo(headerCells);
    }

    @Test
    void Finding_header_cell_by_id_must_return_optional_found() {
        HeaderCell headerCell = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("original_name1")
                .category(Category.NAME)
                .isProcessing(true)
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(headerCell));

        Optional<HeaderCell> found = service.findById(1L);

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(headerCell);
    }

    @Test
    void Creating_header_cell_must_save_and_return_it() {
        // arrange
        HeaderCell headerCell = HeaderCell.builder()
                .subcategoryId(1L)
                .originalName("original_name")
                .category(Category.NAME)
                .isProcessing(true)
                .build();

        HeaderCell savedHeaderCell = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("original_name")
                .category(Category.NAME)
                .isProcessing(true)
                .build();

        Mockito.when(repository.save(headerCell)).thenReturn(savedHeaderCell);

        // act
        HeaderCell result = service.create(headerCell);

        // assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(savedHeaderCell);
    }

    @Test
    void Creating_header_cell_without_category_must_use_category_of_subcategory() {
        // arrange
        Subcategory subcategory = Subcategory.builder()
                .id(1L)
                .category(Category.NAME)
                .build();

        HeaderCell headerCell = HeaderCell.builder()
                .subcategoryId(1L)
                .originalName("original_name")
                .isProcessing(true)
                .build();

        HeaderCell savedHeaderCell = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("original_name")
                .category(Category.NAME)
                .isProcessing(true)
                .build();

        Mockito.when(subcategoryService.findById(1L))
                .thenReturn(Optional.of(subcategory));
        Mockito.when(repository.save(headerCell))
                .thenReturn(savedHeaderCell);

        // act
        HeaderCell result = service.create(headerCell);

        // assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(savedHeaderCell);
        Mockito.verify(subcategoryService, times(1))
                .findById(1L);
        Mockito.verify(repository, times(1))
                .save(headerCell);
    }

    @Test
    void Updating_header_cell_must_update_and_return_it() {
        // arrange
        HeaderCell existingHeaderCell = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("original_name")
                .category(Category.NAME)
                .isProcessing(true)
                .build();

        HeaderCell updatedHeaderCell = HeaderCell.builder()
                .id(1L)
                .subcategoryId(2L)
                .originalName("updated_original_name")
                .category(Category.PRICE)
                .isProcessing(false)
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(existingHeaderCell));
        Mockito.when(repository.update(existingHeaderCell))
                .thenReturn(existingHeaderCell);

        // act
        HeaderCell result = service.update(updatedHeaderCell);

        // assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(updatedHeaderCell);
        Mockito.verify(repository, times(1))
                .findById(1L);
        Mockito.verify(repository, times(1))
                .update(existingHeaderCell);
    }

    @Test
    void Updating_header_cell_without_category_but_with_subcategory_must_use_category_of_subcategory_and_update_it() {
        // arrange
        Subcategory subcategory = Subcategory.builder()
                .id(1L)
                .category(Category.PRICE)
                .build();

        HeaderCell existingHeaderCell = HeaderCell.builder()
                .id(1L)
                .originalName("original_name")
                .category(Category.NAME)
                .isProcessing(true)
                .build();

        HeaderCell updatedHeaderCell = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("updated_original_name")
                .isProcessing(false)
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(existingHeaderCell));
        Mockito.when(subcategoryService.findById(1L))
                .thenReturn(Optional.of(subcategory));
        Mockito.when(repository.update(existingHeaderCell))
                .thenReturn(existingHeaderCell);

        // act
        HeaderCell result = service.update(updatedHeaderCell);

        HeaderCell expected = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("updated_original_name")
                .category(Category.PRICE)
                .isProcessing(false)
                .build();

        // assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);
        Mockito.verify(repository, times(1))
                .findById(1L);
        Mockito.verify(subcategoryService, times(1))
                .findById(1L);
        Mockito.verify(repository, times(1))
                .update(existingHeaderCell);
    }

    @Test
    void Updating_header_cell_without_subcategory_but_with_category_must_use_category_and_update_it() {
        // arrange
        HeaderCell existingHeaderCell = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("original_name")
                .isProcessing(true)
                .build();

        HeaderCell updatedHeaderCell = HeaderCell.builder()
                .id(1L)
                .originalName("updated_original_name")
                .category(Category.PRICE)
                .isProcessing(false)
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(existingHeaderCell));
        Mockito.when(repository.update(existingHeaderCell))
                .thenReturn(existingHeaderCell);

        // act
        HeaderCell result = service.update(updatedHeaderCell);

        // assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(updatedHeaderCell);
        Mockito.verify(repository, times(1))
                .findById(1L);
        Mockito.verify(repository, times(1))
                .update(existingHeaderCell);
    }

    @Test
    void Batch_inserting_collection_of_header_cells_must_save_it_by_one_operations() {
        // arrange
        HeaderCell headerCell1 = HeaderCell.builder()
                .subcategoryId(1L)
                .originalName("original_name1")
                .category(Category.NAME)
                .isProcessing(true)
                .build();

        HeaderCell headerCell2 = HeaderCell.builder()
                .subcategoryId(2L)
                .originalName("original_name2")
                .category(Category.PRICE)
                .isProcessing(false)
                .build();

        List<HeaderCell> headerCells = List.of(headerCell1, headerCell2);

        // act
        service.batchInsert(headerCells);

        // assert
        Mockito.verify(repository, times(1))
                .batchInsert(headerCells);
    }

    @Test
    void Deleting_header_cell_by_id_must_delete_specified_header_cell() {
        service.deleteById(1L);

        Mockito.verify(repository, times(1))
                .deleteById(1L);
    }
    
}
