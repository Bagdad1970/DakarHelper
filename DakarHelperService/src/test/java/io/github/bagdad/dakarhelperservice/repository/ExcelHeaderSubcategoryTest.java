package io.github.bagdad.dakarhelperservice.repository;

import io.github.bagdad.dakarhelperservice.DakarHelperTestConfiguration;
import io.github.bagdad.dakarhelperservice.exception.ExcelHeaderSubcategoryNotFoundException;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderSubcategory;
import io.github.bagdad.dakarhelperservice.repository.implementation.ExcelHeaderSubcategoryRepositoryImpl;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ExcelHeaderSubcategoryRepository;
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
class ExcelHeaderSubcategoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ExcelHeaderSubcategoryRepository repository;

    @BeforeEach
    void setUp() {
        this.repository = new ExcelHeaderSubcategoryRepositoryImpl(jdbcTemplate);
    }

    private static ExcelHeaderSubcategory createExcelHeaderSubcategoryForTesting() {
        ExcelHeaderSubcategory subcategory = new ExcelHeaderSubcategory();
        subcategory.setSubcategoryName("subcategory_name");

        return subcategory;
    }

    @Test
    void Saving_subcategory_must_save_and_return_saved_entity() {
        ExcelHeaderSubcategory subcategory = new ExcelHeaderSubcategory();
        subcategory.setSubcategoryName("subcategory_name");

        ExcelHeaderSubcategory saved = repository.save(subcategory);
        Optional<ExcelHeaderSubcategory> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(saved);
    }

    @Test
    void Finding_all_subcategories_must_return_existing_entities() {
        List<ExcelHeaderSubcategory> subcategories = new ArrayList<>();
        ExcelHeaderSubcategory subcategory1 = createExcelHeaderSubcategoryForTesting();
        ExcelHeaderSubcategory subcategory2 = createExcelHeaderSubcategoryForTesting();
        subcategories.add(subcategory1);
        subcategories.add(subcategory2);

        ExcelHeaderSubcategory savedSubcategory1 = repository.save(subcategory1);
        ExcelHeaderSubcategory savedSubcategory2 = repository.save(subcategory2);

        List<ExcelHeaderSubcategory> found = repository.findAll();

        assertThat(found).isNotEmpty();
        assertThat(found).isEqualTo(List.of(savedSubcategory1, savedSubcategory2));
    }

    @Test
    void Finding_existing_subcategory_by_id_must_return_existing_entity() {
        ExcelHeaderSubcategory subcategory = createExcelHeaderSubcategoryForTesting();

        ExcelHeaderSubcategory savedSubcategory = repository.save(subcategory);

        Optional<ExcelHeaderSubcategory> foundedExcelHeaderSubcategory = repository.findById(savedSubcategory.getId());

        assertThat(foundedExcelHeaderSubcategory).isPresent();
        assertThat(foundedExcelHeaderSubcategory.get()).isEqualTo(savedSubcategory);
    }

    @Test
    void Finding_non_existing_subcategory_by_id_must_return_empty() {
        Optional<ExcelHeaderSubcategory> vendor = repository.findById(0L);

        assertThat(vendor).isEmpty();
    }

    @Test
    void Updating_existing_subcategory_must_update_and_return_updated_entity() {
        ExcelHeaderSubcategory original = new ExcelHeaderSubcategory();
        original.setSubcategoryName("original_title");
        ExcelHeaderSubcategory saved = repository.save(original);

        saved.setSubcategoryName("updated_title");
        ExcelHeaderSubcategory updatedExcelHeaderSubcategory = repository.update(saved);

        Optional<ExcelHeaderSubcategory> foundedExcelHeaderSubcategory = repository.findById(updatedExcelHeaderSubcategory.getId());

        assertThat(foundedExcelHeaderSubcategory).isPresent();
        assertThat(foundedExcelHeaderSubcategory.get()).isEqualTo(saved);
    }

    @Test
    void Updating_non_existing_subcategory_must_throw_exception() {
        ExcelHeaderSubcategory vendor = new ExcelHeaderSubcategory();
        vendor.setId(0L);

        assertThatThrownBy(() -> repository.update(vendor))
                .isInstanceOf(ExcelHeaderSubcategoryNotFoundException.class);
    }

    @Test
    void Deleting_existing_subcategory_must_remove_it() {
        ExcelHeaderSubcategory vendor = new ExcelHeaderSubcategory();
        vendor.setSubcategoryName("subcategory_name");
        ExcelHeaderSubcategory saved = repository.save(vendor);

        repository.delete(saved.getId());

        Optional<ExcelHeaderSubcategory> deleted = repository.findById(saved.getId());

        assertThat(deleted).isEmpty();
    }

    @Test
    void Deleting_non_existing_subcategory_must_throw_exception() {
        Long nonExistingId = 0L;

        assertThatThrownBy(() -> repository.delete(nonExistingId))
                .isInstanceOf(ExcelHeaderSubcategoryNotFoundException.class);
    }
}