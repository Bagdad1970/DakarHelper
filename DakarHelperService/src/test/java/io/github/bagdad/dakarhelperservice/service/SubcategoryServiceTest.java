package io.github.bagdad.dakarhelperservice.service;

import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.repository.implementation.SubcategoryRepositoryImpl;
import io.github.bagdad.dakarhelperservice.service.implementation.SubcategoryServiceImpl;
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
public class SubcategoryServiceTest {

    @Mock
    private SubcategoryRepositoryImpl repository;

    @InjectMocks
    private SubcategoryServiceImpl service;

    @Test
    void findAll() {
        Subcategory subcategory1 = new Subcategory(1L, Category.NAME, "name1");
        Subcategory subcategory2 = new Subcategory(2L,  Category.NAME, "name2");

        Mockito.when(repository.findAll())
                .thenReturn(List.of(subcategory1, subcategory2));

        List<Subcategory> subcategories = service.findAll();

        assertThat(subcategories).isNotNull();
        assertThat(subcategories).hasSize(2);
    }

    @Test
    void findById() {
        Subcategory subcategory = Subcategory.builder()
                .category(Category.NAME)
                .name("name")
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(subcategory));

        Optional<Subcategory> found = service.findById(1L);

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(subcategory);
    }

    @Test
    void create() {
        Subcategory subcategory = Subcategory.builder()
                .category(Category.NAME)
                .name("name")
                .build();
        Subcategory savedSubcategory = Subcategory.builder()
                .id(1L)
                .category(Category.NAME)
                .name("name")
                .build();

        Mockito.when(repository.save(subcategory)).thenReturn(savedSubcategory);

        Subcategory result = service.create(subcategory);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(savedSubcategory);
    }

    @Test
    void update() {
        // arrange
        Subcategory existingSubcategory = Subcategory.builder()
                .id(1L)
                .category(Category.NAME)
                .name("name")
                .build();

        Subcategory updatedSubcategory = Subcategory.builder()
                .id(1L)
                .category(Category.NAME)
                .name("updated_name")
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(existingSubcategory));
        Mockito.when(repository.update(existingSubcategory))
                .thenReturn(existingSubcategory);

        // act
        Subcategory result = service.update(updatedSubcategory);

        // assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(updatedSubcategory);
        Mockito.verify(repository, times(1))
                .findById(1L);
        Mockito.verify(repository, times(1))
                .update(existingSubcategory);
    }

    @Test
    void deleteById() {
        service.deleteById(1L);

        Mockito.verify(repository, times(1))
                .deleteById(1L);
    }

}
