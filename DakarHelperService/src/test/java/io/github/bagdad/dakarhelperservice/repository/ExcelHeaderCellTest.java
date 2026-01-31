package io.github.bagdad.dakarhelperservice.repository;

import io.github.bagdad.dakarhelperservice.DakarHelperTestConfiguration;
import io.github.bagdad.dakarhelperservice.exception.ExcelHeaderCellNotFoundException;
import io.github.bagdad.dakarhelperservice.exception.ExcelHeaderSubcategoryNotFoundException;
import io.github.bagdad.dakarhelperservice.exception.VendorNotFoundException;
import io.github.bagdad.dakarhelperservice.model.*;
import io.github.bagdad.dakarhelperservice.repository.implementation.ExcelHeaderCellRepositoryImpl;
import io.github.bagdad.dakarhelperservice.repository.implementation.ExcelHeaderSubcategoryRepositoryImpl;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ExcelHeaderCellRepository;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ExcelHeaderSubcategoryRepository;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import org.assertj.core.api.SoftAssertions;
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
public class ExcelHeaderCellTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ExcelHeaderSubcategoryRepository excelHeaderSubcategoryRepository;
    private ExcelHeaderCellRepository excelHeaderCellRepository;

    @BeforeEach
    void setUp() {
        this.excelHeaderCellRepository = new ExcelHeaderCellRepositoryImpl(jdbcTemplate);
        this.excelHeaderSubcategoryRepository = new ExcelHeaderSubcategoryRepositoryImpl(jdbcTemplate);
    }

    private static ExcelHeaderSubcategory createSubcategoryForTesting() {
        ExcelHeaderSubcategory subcategory = new ExcelHeaderSubcategory();
        subcategory.setSubcategoryName("subcategory_name");

        return subcategory;
    }

    private static ExcelHeaderCell createExcelHeaderCellForTesting(ExcelHeaderSubcategory excelHeaderSubcategory) {
        Long subcategoryId = excelHeaderSubcategory != null ? excelHeaderSubcategory.getId() : null;
        
        ExcelHeaderCell excelHeaderCell = new ExcelHeaderCell();
        excelHeaderCell.setExcelHeaderSubcategoryId(subcategoryId);
        excelHeaderCell.setOriginName("origin_name");
        excelHeaderCell.setNormalizedName("normalized_name");
        excelHeaderCell.setCategory(Category.NAME);
        excelHeaderCell.setCellStatus(CellStatus.PROCESSED);

        return excelHeaderCell;
    }

    @Test
    void Saving_excel_header_cell_must_save_and_return_saved_excel_header_cell() {
        ExcelHeaderSubcategory subcategory = createSubcategoryForTesting();
        ExcelHeaderSubcategory savedSubcategory = excelHeaderSubcategoryRepository.save(subcategory);

        ExcelHeaderCell vendorFile = createExcelHeaderCellForTesting(savedSubcategory);
        ExcelHeaderCell savedHeaderCell = excelHeaderCellRepository.save(vendorFile);

        Optional<ExcelHeaderCell> found = excelHeaderCellRepository.findById(savedHeaderCell.getId());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(savedHeaderCell);
    }

    @Test
    void Batch_inserting_excel_header_cells_must_save_all() {
        ExcelHeaderSubcategory subcategory1 = createSubcategoryForTesting();
        ExcelHeaderSubcategory subcategory2 = createSubcategoryForTesting();
        ExcelHeaderSubcategory subcategory3 = createSubcategoryForTesting();

        ExcelHeaderSubcategory savedSubcategory1 = excelHeaderSubcategoryRepository.save(subcategory1);
        ExcelHeaderSubcategory savedSubcategory2 = excelHeaderSubcategoryRepository.save(subcategory2);
        ExcelHeaderSubcategory savedSubcategory3 = excelHeaderSubcategoryRepository.save(subcategory3);

        ExcelHeaderCell headerCell1 = createExcelHeaderCellForTesting(savedSubcategory1);
        ExcelHeaderCell headerCell2 = createExcelHeaderCellForTesting(savedSubcategory2);
        ExcelHeaderCell headerCell3 = createExcelHeaderCellForTesting(savedSubcategory3);

        List<ExcelHeaderCell> vendorFiles = new ArrayList<>();
        vendorFiles.add(headerCell1);
        vendorFiles.add(headerCell2);
        vendorFiles.add(headerCell3);

        excelHeaderCellRepository.batchInsert(vendorFiles);

        List<ExcelHeaderCell> saved = excelHeaderCellRepository.findAll();

        assertThat(saved).hasSize(vendorFiles.size());
    }

    @Test
    void Finding_all_excel_header_cells_must_return_existing_entities() {
        ExcelHeaderSubcategory subcategory = createSubcategoryForTesting();
        ExcelHeaderSubcategory savedSubcategory = excelHeaderSubcategoryRepository.save(subcategory);

        ExcelHeaderCell headerCell = createExcelHeaderCellForTesting(savedSubcategory);
        ExcelHeaderCell saved = excelHeaderCellRepository.save(headerCell);

        List<ExcelHeaderCell> files = excelHeaderCellRepository.findAll();

        assertThat(files).isNotEmpty();
        assertThat(files).isEqualTo(List.of(saved));
    }

    @Test
    void Finding_existing_excel_header_cell_by_id_must_return_existing_entity() {
        ExcelHeaderSubcategory subcategory = createSubcategoryForTesting();
        ExcelHeaderSubcategory savedSubcategory = excelHeaderSubcategoryRepository.save(subcategory);

        ExcelHeaderCell headerCell = createExcelHeaderCellForTesting(savedSubcategory);
        ExcelHeaderCell savedHeaderCell = excelHeaderCellRepository.save(headerCell);

        Optional<ExcelHeaderCell> found = excelHeaderCellRepository.findById(savedHeaderCell.getId());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(savedHeaderCell);
    }

    @Test
    void Finding_non_existing_excel_header_cell_by_id_must_return_empty() {
        Optional<ExcelHeaderCell> found = excelHeaderCellRepository.findById(0L);

        assertThat(found).isEmpty();
    }

    @Test
    void Updating_existing_excel_header_cell_must_update_and_return_updated_entity() {
        ExcelHeaderSubcategory vendor1 = createSubcategoryForTesting();
        ExcelHeaderSubcategory savedSubcategory1 = excelHeaderSubcategoryRepository.save(vendor1);

        ExcelHeaderSubcategory vendor2 = createSubcategoryForTesting();
        ExcelHeaderSubcategory savedSubcategory2 = excelHeaderSubcategoryRepository.save(vendor2);

        ExcelHeaderCell headerCell = createExcelHeaderCellForTesting(savedSubcategory1);
        ExcelHeaderCell savedHeaderCell = excelHeaderCellRepository.save(headerCell);

        savedHeaderCell.setExcelHeaderSubcategoryId(savedSubcategory2.getId());
        savedHeaderCell.setOriginName("updated_origin_name");
        savedHeaderCell.setNormalizedName("updated_normalized_name");
        savedHeaderCell.setCategory(Category.PRICE);
        savedHeaderCell.setCellStatus(CellStatus.IGNORED);
        ExcelHeaderCell updatedExcelHeaderCell = excelHeaderCellRepository.update(savedHeaderCell);

        Optional<ExcelHeaderCell> found = excelHeaderCellRepository.findById(updatedExcelHeaderCell.getId());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(savedHeaderCell);
    }

    @Test
    void Updating_non_existing_excel_header_cell_must_throw_exception() {
        ExcelHeaderCell nonExistent = new ExcelHeaderCell();
        nonExistent.setId(0L);
        nonExistent.setCategory(Category.NAME);
        nonExistent.setCellStatus(CellStatus.PROCESSED);

        assertThatThrownBy(() -> excelHeaderCellRepository.update(nonExistent))
                .isInstanceOf(ExcelHeaderCellNotFoundException.class);
    }

    @Test
    void Deleting_existing_excel_header_cell_must_remove_it() {
        ExcelHeaderSubcategory subcategory = createSubcategoryForTesting();

        ExcelHeaderSubcategory savedSubcategory = excelHeaderSubcategoryRepository.save(subcategory);

        ExcelHeaderCell excelHeaderCell = createExcelHeaderCellForTesting(savedSubcategory);
        ExcelHeaderCell saved = excelHeaderCellRepository.save(excelHeaderCell);

        excelHeaderCellRepository.delete(saved.getId());

        Optional<ExcelHeaderCell> deleted = excelHeaderCellRepository.findById(saved.getId());

        assertThat(deleted).isEmpty();
    }

    @Test
    void Deleting_non_existing_excel_header_cell_must_throw_exception() {
        Long nonExistingId = 0L;

        assertThatThrownBy(() -> excelHeaderCellRepository.delete(nonExistingId))
                .isInstanceOf(ExcelHeaderCellNotFoundException.class);
    }

}
