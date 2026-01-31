package io.github.bagdad.dakarhelperservice.repository.implementation;

import io.github.bagdad.dakarhelperservice.exception.ExcelHeaderCellNotFoundException;
import io.github.bagdad.dakarhelperservice.exception.VendorNotFoundException;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderCell;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderCellWithSubcategory;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderSubcategory;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ExcelHeaderCellRepository;
import io.github.bagdad.dakarhelperservice.repository.mapper.ExcelHeaderCellMapper;
import io.github.bagdad.dakarhelperservice.repository.mapper.ExcelHeaderCellWithSubcategoryMapper;
import io.github.bagdad.models.excelparser.Category;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ExcelHeaderCellRepositoryImpl implements ExcelHeaderCellRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final ExcelHeaderCellMapper EXCEL_HEADER_CELL_MAPPER = new ExcelHeaderCellMapper();
    private static final ExcelHeaderCellWithSubcategoryMapper EXCEL_HEADER_CELL_WITH_SUBCATEGORY_MAPPER = new ExcelHeaderCellWithSubcategoryMapper();

    public ExcelHeaderCellRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ExcelHeaderCell save(ExcelHeaderCell excelHeaderCell) {
        String sql = """
            INSERT INTO excel_header_cells (
                excel_header_subcategory_id,
                origin_name,
                normalized_name,
                category,
                cell_status
            ) VALUES (?, ?, ?, ?, ?)
            RETURNING
                id,
                excel_header_subcategory_id,
                origin_name,
                normalized_name,
                category,
                cell_status
        """;

        return jdbcTemplate.queryForObject(
                sql,
                EXCEL_HEADER_CELL_MAPPER,
                excelHeaderCell.getExcelHeaderSubcategoryId(),
                excelHeaderCell.getOriginName(),
                excelHeaderCell.getNormalizedName(),
                excelHeaderCell.getCategory().name(),
                excelHeaderCell.getCellStatus().name()
        );
    }

    @Override
    public void batchInsert(List<ExcelHeaderCell> excelHeaderCells) {
        String sql = """
            INSERT INTO excel_header_cells (
                excel_header_subcategory_id,
                origin_name,
                normalized_name,
                category,
                cell_status
            ) VALUES (?, ?, ?, ?, ?)
        """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ExcelHeaderCell excelHeaderCell = excelHeaderCells.get(i);
                ps.setLong(1, excelHeaderCell.getExcelHeaderSubcategoryId());
                ps.setString(2, excelHeaderCell.getOriginName());
                ps.setString(3, excelHeaderCell.getNormalizedName());
                ps.setString(4, excelHeaderCell.getCategory() != null ? excelHeaderCell.getCategory().name() : null);
                ps.setString(5, excelHeaderCell.getCellStatus() != null ? excelHeaderCell.getCellStatus().name() : null);
            }

            @Override
            public int getBatchSize() {
                return excelHeaderCells.size();
            }
        });
    }

    @Override
    public ExcelHeaderCell update(ExcelHeaderCell excelHeaderCell) {
        String sql = """
            UPDATE excel_header_cells SET
                excel_header_subcategory_id = ?,
                origin_name = ?,
                normalized_name = ?,
                category = ?,
                cell_status = ?
            WHERE id = ?
            RETURNING
                id,
                excel_header_subcategory_id,
                origin_name,
                normalized_name,
                category,
                cell_status
        """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    EXCEL_HEADER_CELL_MAPPER,
                    excelHeaderCell.getExcelHeaderSubcategoryId(),
                    excelHeaderCell.getOriginName(),
                    excelHeaderCell.getNormalizedName(),
                    excelHeaderCell.getCategory().name(),
                    excelHeaderCell.getCellStatus().name(),
                    excelHeaderCell.getId()
            );
        }
        catch (EmptyResultDataAccessException e) {
            throw new ExcelHeaderCellNotFoundException(excelHeaderCell.getId());
        }

    }

    @Override
    public List<ExcelHeaderCell> findAll() {
        String sql = """
            SELECT
                id,
                excel_header_subcategory_id,
                origin_name,
                normalized_name,
                category,
                cell_status
            FROM excel_header_cells
        """;

        return jdbcTemplate.query(
                sql,
                EXCEL_HEADER_CELL_MAPPER
        );
    }

    @Override
    public Optional<ExcelHeaderCell> findById(Long id) {
        String sql = """
            SELECT
                id,
                excel_header_subcategory_id,
                origin_name,
                normalized_name,
                category,
                cell_status
            FROM excel_header_cells
            WHERE id = ?
        """;

        try {
            ExcelHeaderCell excelHeaderCell = jdbcTemplate.queryForObject(
                    sql,
                    EXCEL_HEADER_CELL_MAPPER,
                    id
            );
            return Optional.ofNullable(excelHeaderCell);
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<ExcelHeaderCellWithSubcategory> findAllWithSubcategory() {
        String sql = """
            SELECT
                c.id,
                c.excel_header_subcategory_id,
                c.origin_name,
                c.normalized_name,
                c.category,
                c.cell_status,
                s.id AS sub_id,
                s.subcategory_name
            FROM excel_header_cells c
            LEFT JOIN excel_header_subcategories s ON c.excel_header_subcategory_id = s.id
        """;

        return jdbcTemplate.query(
                sql,
                EXCEL_HEADER_CELL_WITH_SUBCATEGORY_MAPPER
        );
    }

    @Override
    public List<ExcelHeaderCell> findAllByCategory(Category category) {
        String sql = """
            SELECT
                id,
                excel_header_subcategory_id,
                origin_name,
                normalized_name,
                category,
                cell_status
            FROM excel_header_cells
            WHERE category = ?
        """;

        return jdbcTemplate.query(
                sql,
                EXCEL_HEADER_CELL_MAPPER,
                category.name()
        );
    }

    @Override
    public void delete(Long id) {
        String sql = """
            DELETE FROM excel_header_cells
            WHERE id = ?
        """;

        int count = jdbcTemplate.update(
                sql,
                id
        );

        if (count == 0) {
            throw new ExcelHeaderCellNotFoundException(id);
        }
    }

}
