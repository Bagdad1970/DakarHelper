package io.github.bagdad.dakarhelperservice.repository.implementation;

import io.github.bagdad.dakarhelperservice.exception.SubcategoryNotFoundException;
import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.dakarhelperservice.repository.interfaces.SubcategoryRepository;
import io.github.bagdad.dakarhelperservice.repository.mapper.SubcategoryMapper;
import io.github.bagdad.models.excelparser.Category;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SubcategoryRepositoryImpl implements SubcategoryRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final SubcategoryMapper EXCEL_HEADER_SUBCATEGORY_MAPPER = new SubcategoryMapper();

    public SubcategoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Subcategory save(Subcategory subcategory) {
        String sql = """
            INSERT INTO subcategories (
                category,
                name
            ) VALUES (?, ?)
            RETURNING
                id,
                category,
                name
        """;

        return jdbcTemplate.queryForObject(
                sql,
                EXCEL_HEADER_SUBCATEGORY_MAPPER,
                subcategory.getCategory().name(),
                subcategory.getName()
        );
    }

    @Override
    public Subcategory update(Subcategory subcategory) {
        String sql = """
            UPDATE subcategories SET
                category = ?,
                name = ?
            WHERE id = ?
            RETURNING
                id,
                category,
                name
        """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    EXCEL_HEADER_SUBCATEGORY_MAPPER,
                    subcategory.getCategory().name(),
                    subcategory.getName(),
                    subcategory.getId()
            );
        }
        catch (EmptyResultDataAccessException e) {
            throw new SubcategoryNotFoundException(subcategory.getId());
        }
    }

    @Override
    public List<Subcategory> findAll() {
        String sql = """
            SELECT 
                id,
                category,
                name
            FROM subcategories
        """;

        return jdbcTemplate.query(
                sql,
                EXCEL_HEADER_SUBCATEGORY_MAPPER
        );
    }

    @Override
    public List<Subcategory> findAllByCategory(Category category) {
        String sql = """
            SELECT 
                id,
                name
            FROM subcategories
            WHERE category = ?
        """;

        return jdbcTemplate.query(
                sql,
                EXCEL_HEADER_SUBCATEGORY_MAPPER,
                category
        );
    }

    @Override
    public Optional<Subcategory> findById(Long id) {
        String sql = """
            SELECT
                id,
                category,
                name
            FROM subcategories
            WHERE id = ?
        """;

        try {
            Subcategory Subcategory = jdbcTemplate.queryForObject(
                    sql,
                    EXCEL_HEADER_SUBCATEGORY_MAPPER,
                    id
            );
            return Optional.ofNullable(Subcategory);
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM subcategories WHERE id = ?";

        int count = jdbcTemplate.update(
                sql,
                id
        );

        if (count == 0) {
            throw new SubcategoryNotFoundException(id);
        }
    }
    
}
