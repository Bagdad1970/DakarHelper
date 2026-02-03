package io.github.bagdad.dakarhelperservice.repository.implementation;

import io.github.bagdad.dakarhelperservice.exception.HeaderCellNotFoundException;
import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.model.HeaderCellWithSubcategory;
import io.github.bagdad.dakarhelperservice.repository.interfaces.HeaderCellRepository;
import io.github.bagdad.dakarhelperservice.repository.mapper.HeaderCellMapper;
import io.github.bagdad.dakarhelperservice.repository.mapper.HeaderCellWithSubcategoryMapper;
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
public class HeaderCellRepositoryImpl implements HeaderCellRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final HeaderCellMapper EXCEL_HEADER_CELL_MAPPER = new HeaderCellMapper();
    private static final HeaderCellWithSubcategoryMapper EXCEL_HEADER_CELL_WITH_SUBCATEGORY_MAPPER = new HeaderCellWithSubcategoryMapper();

    public HeaderCellRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public HeaderCell save(HeaderCell headerCell) {
        String sql = """
            INSERT INTO header_cells (
                subcategory_id,
                original_name,
                normalized_name,
                category,
                cell_status
            ) VALUES (?, ?, ?, ?, ?)
            RETURNING
                id,
                subcategory_id,
                original_name,
                normalized_name,
                category,
                cell_status
        """;

        return jdbcTemplate.queryForObject(
                sql,
                EXCEL_HEADER_CELL_MAPPER,
                headerCell.getSubcategoryId(),
                headerCell.getOriginalName(),
                headerCell.getNormalizedName(),
                headerCell.getCategory().name(),
                headerCell.getCellStatus().name()
        );
    }

    @Override
    public void batchInsert(List<HeaderCell> headerCells) {
        String sql = """
            INSERT INTO header_cells (
                subcategory_id,
                original_name,
                normalized_name,
                category,
                cell_status
            ) VALUES (?, ?, ?, ?, ?)
        """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                HeaderCell headerCell = headerCells.get(i);
                ps.setLong(1, headerCell.getSubcategoryId());
                ps.setString(2, headerCell.getOriginalName());
                ps.setString(3, headerCell.getNormalizedName());
                ps.setString(4, headerCell.getCategory() != null ? headerCell.getCategory().name() : null);
                ps.setString(5, headerCell.getCellStatus() != null ? headerCell.getCellStatus().name() : null);
            }

            @Override
            public int getBatchSize() {
                return headerCells.size();
            }
        });
    }

    @Override
    public HeaderCell update(HeaderCell headerCell) {
        String sql = """
            UPDATE header_cells SET
                subcategory_id = ?,
                original_name = ?,
                normalized_name = ?,
                category = ?,
                cell_status = ?
            WHERE id = ?
            RETURNING
                id,
                subcategory_id,
                original_name,
                normalized_name,
                category,
                cell_status
        """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    EXCEL_HEADER_CELL_MAPPER,
                    headerCell.getSubcategoryId(),
                    headerCell.getOriginalName(),
                    headerCell.getNormalizedName(),
                    headerCell.getCategory().name(),
                    headerCell.getCellStatus().name(),
                    headerCell.getId()
            );
        }
        catch (EmptyResultDataAccessException e) {
            throw new HeaderCellNotFoundException(headerCell.getId());
        }

    }

    @Override
    public List<HeaderCell> findAll() {
        String sql = """
            SELECT
                id,
                subcategory_id,
                original_name,
                normalized_name,
                category,
                cell_status
            FROM header_cells
        """;

        return jdbcTemplate.query(
                sql,
                EXCEL_HEADER_CELL_MAPPER
        );
    }

    @Override
    public Optional<HeaderCell> findById(Long id) {
        String sql = """
            SELECT
                id,
                subcategory_id,
                original_name,
                normalized_name,
                category,
                cell_status
            FROM header_cells
            WHERE id = ?
        """;

        try {
            HeaderCell headerCell = jdbcTemplate.queryForObject(
                    sql,
                    EXCEL_HEADER_CELL_MAPPER,
                    id
            );
            return Optional.ofNullable(headerCell);
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<HeaderCellWithSubcategory> findAllWithSubcategory() {
        String sql = """
            SELECT
                c.id,
                c.subcategory_id,
                c.original_name,
                c.normalized_name,
                c.category,
                c.cell_status,
                s.id AS sub_id,
                s.name
            FROM header_cells c
            LEFT JOIN subcategories s ON c.subcategory_id = s.id
        """;

        return jdbcTemplate.query(
                sql,
                EXCEL_HEADER_CELL_WITH_SUBCATEGORY_MAPPER
        );
    }

    @Override
    public List<HeaderCell> findAllByCategory(Category category) {
        String sql = """
            SELECT
                id,
                subcategory_id,
                original_name,
                normalized_name,
                category,
                cell_status
            FROM header_cells
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
            DELETE FROM header_cells
            WHERE id = ?
        """;

        int count = jdbcTemplate.update(
                sql,
                id
        );

        if (count == 0) {
            throw new HeaderCellNotFoundException(id);
        }
    }

}
