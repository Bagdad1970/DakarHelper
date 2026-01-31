package io.github.bagdad.dakarhelperservice.repository.implementation;

import io.github.bagdad.dakarhelperservice.exception.ExcelHeaderCellNotFoundException;
import io.github.bagdad.dakarhelperservice.exception.ExcelHeaderSubcategoryNotFoundException;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderSubcategory;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ExcelHeaderSubcategoryRepository;
import io.github.bagdad.dakarhelperservice.repository.mapper.ExcelHeaderSubcategoryMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ExcelHeaderSubcategoryRepositoryImpl implements ExcelHeaderSubcategoryRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final ExcelHeaderSubcategoryMapper EXCEL_HEADER_SUBCATEGORY_MAPPER = new ExcelHeaderSubcategoryMapper();

    public ExcelHeaderSubcategoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ExcelHeaderSubcategory save(ExcelHeaderSubcategory excelHeaderSubcategory) {
        String sql = """
            INSERT INTO excel_header_subcategories (
                subcategory_name
            ) VALUES (?)
            RETURNING
                id,
                subcategory_name
        """;

        return jdbcTemplate.queryForObject(
                sql,
                EXCEL_HEADER_SUBCATEGORY_MAPPER,
                excelHeaderSubcategory.getSubcategoryName()
        );
    }

    @Override
    public ExcelHeaderSubcategory update(ExcelHeaderSubcategory excelHeaderSubcategory) {
        String sql = """
            UPDATE excel_header_subcategories SET
                subcategory_name = ?
            WHERE id = ?
            RETURNING
                id,
                subcategory_name
        """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    EXCEL_HEADER_SUBCATEGORY_MAPPER,
                    excelHeaderSubcategory.getSubcategoryName(),
                    excelHeaderSubcategory.getId()
            );
        }
        catch (EmptyResultDataAccessException e) {
            throw new ExcelHeaderSubcategoryNotFoundException(excelHeaderSubcategory.getId());
        }
    }

    @Override
    public List<ExcelHeaderSubcategory> findAll() {
        String sql = """
            SELECT id, subcategory_name
            FROM excel_header_subcategories
        """;

        return jdbcTemplate.query(
                sql,
                EXCEL_HEADER_SUBCATEGORY_MAPPER
        );
    }

    @Override
    public Optional<ExcelHeaderSubcategory> findById(Long id) {
        String sql = """
            SELECT id, subcategory_name
            FROM excel_header_subcategories
            WHERE id = ?
        """;

        try {
            ExcelHeaderSubcategory ExcelHeaderSubcategory = jdbcTemplate.queryForObject(
                    sql,
                    EXCEL_HEADER_SUBCATEGORY_MAPPER,
                    id
            );
            return Optional.ofNullable(ExcelHeaderSubcategory);
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM excel_header_subcategories WHERE id = ?";

        int count = jdbcTemplate.update(
                sql,
                id
        );

        if (count == 0) {
            throw new ExcelHeaderSubcategoryNotFoundException(id);
        }
    }
    
}
