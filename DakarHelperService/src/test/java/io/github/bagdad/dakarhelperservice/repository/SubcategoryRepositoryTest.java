package io.github.bagdad.dakarhelperservice.repository;

import io.github.bagdad.findhandler.dakarhelperservice.DakarHelperTestConfiguration;
import io.github.bagdad.dakarhelperservice.exception.SubcategoryNotFoundException;
import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.dakarhelperservice.repository.implementation.SubcategoryRepositoryImpl;
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
class SubcategoryRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SubcategoryRepository repository;

    @BeforeEach
    void setUp() {
        this.repository = new SubcategoryRepositoryImpl(jdbcTemplate);
    }

    private static Subcategory createSubcategoryForTesting() {
        Subcategory subcategory = new Subcategory();
        subcategory.setCategory(Category.NAME);
        subcategory.setName("subcategory_name");

        return subcategory;
    }

    @Test
    void Saving_subcategory_must_save_and_return_saved_entity() {
        Subcategory subcategory = createSubcategoryForTesting();

        Subcategory saved = repository.save(subcategory);

        Optional<Subcategory> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(saved);
    }

    @Test
    void Finding_all_subcategories_must_return_existing_entities() {
        List<Subcategory> subcategories = new ArrayList<>();
        Subcategory subcategory1 = createSubcategoryForTesting();
        Subcategory subcategory2 = createSubcategoryForTesting();
        subcategories.add(subcategory1);
        subcategories.add(subcategory2);

        Subcategory savedSubcategory1 = repository.save(subcategory1);
        Subcategory savedSubcategory2 = repository.save(subcategory2);

        List<Subcategory> found = repository.findAll();

        assertThat(found).isNotEmpty();
        assertThat(found).isEqualTo(List.of(savedSubcategory1, savedSubcategory2));
    }

    @Test
    void Finding_existing_subcategory_by_id_must_return_existing_entity() {
        Subcategory subcategory = createSubcategoryForTesting();

        Subcategory savedSubcategory = repository.save(subcategory);

        Optional<Subcategory> foundedExcelHeaderSubcategory = repository.findById(savedSubcategory.getId());

        assertThat(foundedExcelHeaderSubcategory).isPresent();
        assertThat(foundedExcelHeaderSubcategory.get()).isEqualTo(savedSubcategory);
    }

    @Test
    void Finding_non_existing_subcategory_by_id_must_return_empty() {
        Optional<Subcategory> subcategory = repository.findById(0L);

        assertThat(subcategory).isEmpty();
    }

    @Test
    void Updating_existing_subcategory_must_update_and_return_updated_entity() {
        Subcategory original = createSubcategoryForTesting();

        Subcategory saved = repository.save(original);

        saved.setName("updated_title");
        Subcategory updatedSubcategory = repository.update(saved);

        Optional<Subcategory> found = repository.findById(updatedSubcategory.getId());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(saved);
    }

    @Test
    void Updating_non_existing_subcategory_must_throw_exception() {
        Subcategory subcategory = new Subcategory();
        subcategory.setId(0L);
        subcategory.setCategory(Category.NAME);

        assertThatThrownBy(() -> repository.update(subcategory))
                .isInstanceOf(SubcategoryNotFoundException.class);
    }

    @Test
    void Deleting_existing_subcategory_must_remove_it() {
        Subcategory subcategory = createSubcategoryForTesting();

        Subcategory saved = repository.save(subcategory);

        repository.delete(saved.getId());

        Optional<Subcategory> deleted = repository.findById(saved.getId());

        assertThat(deleted).isEmpty();
    }

    @Test
    void Deleting_non_existing_subcategory_must_throw_exception() {
        Long nonExistingId = 0L;

        assertThatThrownBy(() -> repository.delete(nonExistingId))
                .isInstanceOf(SubcategoryNotFoundException.class);
    }
}