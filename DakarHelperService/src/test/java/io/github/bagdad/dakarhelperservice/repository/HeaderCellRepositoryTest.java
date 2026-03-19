package io.github.bagdad.dakarhelperservice.repository;

import io.github.bagdad.dakarhelperservice.DakarHelperTestConfiguration;
import io.github.bagdad.dakarhelperservice.exception.HeaderCellNotFoundException;
import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.dakarhelperservice.repository.implementation.HeaderCellRepositoryImpl;
import io.github.bagdad.dakarhelperservice.repository.implementation.SubcategoryRepositoryImpl;
import io.github.bagdad.dakarhelperservice.repository.interfaces.HeaderCellRepository;
import io.github.bagdad.dakarhelperservice.repository.interfaces.SubcategoryRepository;
import io.github.bagdad.models.excelparser.Category;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@JdbcTest
@ContextConfiguration(classes = DakarHelperTestConfiguration.class)
public class HeaderCellRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SubcategoryRepository subcategoryRepository;
    private HeaderCellRepository headerCellRepository;

    @BeforeEach
    void setUp() {
        this.headerCellRepository = new HeaderCellRepositoryImpl(jdbcTemplate);
        this.subcategoryRepository = new SubcategoryRepositoryImpl(jdbcTemplate);
    }

    private static Subcategory createSubcategoryForTesting() {
        Subcategory subcategory = new Subcategory();
        subcategory.setCategory(Category.NAME);
        subcategory.setName("subcategory_name");

        return subcategory;
    }

    private static HeaderCell createHeaderCellForTesting(Subcategory subcategory) {
        Long subcategoryId = subcategory != null ? subcategory.getId() : null;
        
        HeaderCell headerCell = new HeaderCell();
        headerCell.setSubcategoryId(subcategoryId);
        headerCell.setOriginalName("original_name");
        headerCell.setCategory(Category.NAME);
        headerCell.setIsProcessing(true);

        return headerCell;
    }

    @Test
    void Saving_header_cell_must_save_and_return_saved_header_cell() {
        Subcategory subcategory = createSubcategoryForTesting();
        Subcategory savedSubcategory = subcategoryRepository.save(subcategory);

        HeaderCell vendorFile = createHeaderCellForTesting(savedSubcategory);
        HeaderCell savedHeaderCell = headerCellRepository.save(vendorFile);

        Optional<HeaderCell> found = headerCellRepository.findById(savedHeaderCell.getId());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(savedHeaderCell);
    }

    @Test
    void Batch_inserting_header_cells_must_save_all() {
        Subcategory subcategory1 = createSubcategoryForTesting();
        Subcategory subcategory2 = createSubcategoryForTesting();
        Subcategory subcategory3 = createSubcategoryForTesting();

        Subcategory savedSubcategory1 = subcategoryRepository.save(subcategory1);
        Subcategory savedSubcategory2 = subcategoryRepository.save(subcategory2);
        Subcategory savedSubcategory3 = subcategoryRepository.save(subcategory3);

        HeaderCell headerCell1 = createHeaderCellForTesting(savedSubcategory1);
        HeaderCell headerCell2 = createHeaderCellForTesting(savedSubcategory2);
        HeaderCell headerCell3 = createHeaderCellForTesting(savedSubcategory3);

        List<HeaderCell> vendorFiles = new ArrayList<>();
        vendorFiles.add(headerCell1);
        vendorFiles.add(headerCell2);
        vendorFiles.add(headerCell3);

        headerCellRepository.batchInsert(vendorFiles);

        List<HeaderCell> saved = headerCellRepository.findAll();

        assertThat(saved).hasSize(vendorFiles.size());
    }

    @Test
    void Finding_all_header_cells_must_return_them() {
        Subcategory subcategory = createSubcategoryForTesting();
        Subcategory savedSubcategory = subcategoryRepository.save(subcategory);

        HeaderCell headerCell = createHeaderCellForTesting(savedSubcategory);
        HeaderCell saved = headerCellRepository.save(headerCell);

        List<HeaderCell> files = headerCellRepository.findAll();

        assertThat(files).isNotEmpty();
        assertThat(files).isEqualTo(List.of(saved));
    }

    @Test
    void Finding_existing_header_cell_by_id_must_return_existing_entity() {
        Subcategory subcategory = createSubcategoryForTesting();
        Subcategory savedSubcategory = subcategoryRepository.save(subcategory);

        HeaderCell headerCell = createHeaderCellForTesting(savedSubcategory);
        HeaderCell savedHeaderCell = headerCellRepository.save(headerCell);

        Optional<HeaderCell> found = headerCellRepository.findById(savedHeaderCell.getId());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(savedHeaderCell);
    }

    @Test
    void Finding_non_existing_header_cell_by_id_must_return_empty() {
        Optional<HeaderCell> found = headerCellRepository.findById(0L);

        assertThat(found).isEmpty();
    }

    @Test
    void Updating_existing_header_cell_must_update_and_return_updated_entity() {
        Subcategory vendor1 = createSubcategoryForTesting();
        Subcategory savedSubcategory1 = subcategoryRepository.save(vendor1);

        Subcategory vendor2 = createSubcategoryForTesting();
        Subcategory savedSubcategory2 = subcategoryRepository.save(vendor2);

        HeaderCell headerCell = createHeaderCellForTesting(savedSubcategory1);
        HeaderCell savedHeaderCell = headerCellRepository.save(headerCell);

        savedHeaderCell.setSubcategoryId(savedSubcategory2.getId());
        savedHeaderCell.setOriginalName("updated_original_name");
        savedHeaderCell.setCategory(Category.PRICE);
        savedHeaderCell.setIsProcessing(false);
        HeaderCell updatedHeaderCell = headerCellRepository.update(savedHeaderCell);

        Optional<HeaderCell> found = headerCellRepository.findById(updatedHeaderCell.getId());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(savedHeaderCell);
    }

    @Test
    void Updating_non_existing_header_cell_must_throw_exception() {
        HeaderCell nonExistent = new HeaderCell();
        nonExistent.setId(0L);
        nonExistent.setCategory(Category.NAME);
        nonExistent.setIsProcessing(true);

        assertThatThrownBy(() -> headerCellRepository.update(nonExistent))
                .isInstanceOf(HeaderCellNotFoundException.class);
    }

    @Test
    void Deleting_existing_header_cell_must_remove_it() {
        Subcategory subcategory = createSubcategoryForTesting();

        Subcategory savedSubcategory = subcategoryRepository.save(subcategory);

        HeaderCell headerCell = createHeaderCellForTesting(savedSubcategory);
        HeaderCell saved = headerCellRepository.save(headerCell);

        headerCellRepository.deleteById(saved.getId());

        Optional<HeaderCell> deleted = headerCellRepository.findById(saved.getId());

        assertThat(deleted).isEmpty();
    }

    @Test
    void Deleting_non_existing_header_cell_must_throw_exception() {
        Long nonExistingId = 0L;

        assertThatThrownBy(() -> headerCellRepository.deleteById(nonExistingId))
                .isInstanceOf(HeaderCellNotFoundException.class);
    }

    @Test
    void Batch_deleting_header_cells_must_delete_specified_header_cells() {
        // arrange
        Subcategory subcategory = createSubcategoryForTesting();

        HeaderCell headerCell1 = createHeaderCellForTesting(subcategory);
        HeaderCell headerCell2 = createHeaderCellForTesting(subcategory);
        HeaderCell headerCell3 = createHeaderCellForTesting(subcategory);

        HeaderCell savedHeaderCell1 = headerCellRepository.save(headerCell1);
        HeaderCell savedHeaderCell2 = headerCellRepository.save(headerCell2);
        HeaderCell savedHeaderCell3 = headerCellRepository.save(headerCell3);

        List<Long> ids = List.of(savedHeaderCell1.getId(), savedHeaderCell2.getId());

        // act
        headerCellRepository.batchDelete(ids);

        // assert
        List<HeaderCell> headerCells = headerCellRepository.findAll();

        assertThat(headerCells).hasSize(1);
        assertThat(headerCells.get(0)).isEqualTo(savedHeaderCell3);
    }

}
