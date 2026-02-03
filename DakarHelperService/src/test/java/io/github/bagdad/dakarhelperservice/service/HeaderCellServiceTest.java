package io.github.bagdad.dakarhelperservice.service;

import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.repository.implementation.HeaderCellRepositoryImpl;
import io.github.bagdad.dakarhelperservice.service.implementation.HeaderCellServiceImpl;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
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

    @InjectMocks
    private HeaderCellServiceImpl service;

    @Test
    void findAll() {
        HeaderCell headerCell1 = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("original_name1")
                .normalizedName("normalized_name1")
                .category(Category.NAME)
                .cellStatus(CellStatus.PROCESSED)
                .build();

        HeaderCell headerCell2 = HeaderCell.builder()
                .id(2L)
                .subcategoryId(2L)
                .originalName("original_name2")
                .normalizedName("normalized_name2")
                .category(Category.PRICE)
                .cellStatus(CellStatus.IGNORED)
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
    void create() {
        // arrange
        HeaderCell headerCell = HeaderCell.builder()
                .subcategoryId(1L)
                .originalName("original_name")
                .normalizedName("normalized_name")
                .category(Category.NAME)
                .cellStatus(CellStatus.PROCESSED)
                .build();

        HeaderCell savedHeaderCell = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("original_name")
                .normalizedName("normalized_name")
                .category(Category.NAME)
                .cellStatus(CellStatus.PROCESSED)
                .build();

        Mockito.when(repository.save(headerCell)).thenReturn(savedHeaderCell);

        // act
        HeaderCell result = service.create(headerCell);

        // assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(savedHeaderCell);
    }

    @Test
    void update() {
        // arrange
        HeaderCell existingHeaderCell = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("original_name")
                .normalizedName("normalized_name")
                .category(Category.NAME)
                .cellStatus(CellStatus.PROCESSED)
                .build();

        HeaderCell updatedHeaderCell = HeaderCell.builder()
                .id(1L)
                .subcategoryId(2L)
                .originalName("updated_original_name")
                .normalizedName("updated_normalized_name")
                .category(Category.PRICE)
                .cellStatus(CellStatus.IGNORED)
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
    void batchInsert() {
        // arrange
        HeaderCell headerCell1 = HeaderCell.builder()
                .subcategoryId(1L)
                .originalName("original_name1")
                .normalizedName("normalized_name1")
                .category(Category.NAME)
                .cellStatus(CellStatus.PROCESSED)
                .build();

        HeaderCell headerCell2 = HeaderCell.builder()
                .subcategoryId(2L)
                .originalName("original_name2")
                .normalizedName("normalized_name2")
                .category(Category.PRICE)
                .cellStatus(CellStatus.IGNORED)
                .build();

        List<HeaderCell> headerCells = List.of(headerCell1, headerCell2);

        // act
        service.batchInsert(headerCells);

        // assert
        Mockito.verify(repository, times(1))
                .batchInsert(headerCells);
    }

    @Test
    void delete() {
        service.delete(1L);

        Mockito.verify(repository, times(1))
                .delete(1L);
    }
    
}
