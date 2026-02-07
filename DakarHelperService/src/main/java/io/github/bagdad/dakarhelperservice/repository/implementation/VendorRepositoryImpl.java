package io.github.bagdad.dakarhelperservice.repository.implementation;

import io.github.bagdad.dakarhelperservice.exception.VendorNotFoundException;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.repository.interfaces.VendorRepository;
import io.github.bagdad.dakarhelperservice.repository.mapper.VendorMapper;
import io.github.bagdad.models.emailhandler.VendorWithMaxFileDateTime;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class VendorRepositoryImpl implements VendorRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final VendorMapper VENDOR_MAPPER = new VendorMapper();

    public VendorRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Vendor save(Vendor vendor) {
        String sql = """
        INSERT INTO vendors (
            title
        ) VALUES (?)
        RETURNING
            id,
            title
        """;

        return jdbcTemplate.queryForObject(
                sql,
                VENDOR_MAPPER,
                vendor.getTitle()
        );
    }

    @Override
    public void batchInsert(List<Vendor> vendors) {
        String sql = """
        INSERT INTO vendors (
            title
        ) VALUES (?)
        """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Vendor vendor = vendors.get(i);
                ps.setString(1, vendor.getTitle());
            }

            @Override
            public int getBatchSize() {
                return vendors.size();
            }
        });
    }

    @Override
    public Vendor update(Vendor vendor) {
        String sql = """
            UPDATE vendors SET
                title = ?
            WHERE id = ?
            RETURNING
                id,
                title
        """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    VENDOR_MAPPER,
                    vendor.getTitle(),
                    vendor.getId()
            );
        }
        catch (EmptyResultDataAccessException e) {
            throw new VendorNotFoundException(vendor.getId());
        }
    }

    @Override
    public List<Vendor> findAll() {
        String sql = """
            SELECT id, title
            FROM vendors
        """;

        return jdbcTemplate.query(
                sql,
                VENDOR_MAPPER
        );
    }

    @Override
    public Optional<Vendor> findById(Long id) {
        String sql = """
            SELECT id, title
            FROM vendors
            WHERE id = ?
        """;

        try {
            Vendor vendor = jdbcTemplate.queryForObject(
                    sql,
                    VENDOR_MAPPER,
                    id
            );
            return Optional.ofNullable(vendor);
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = """
            DELETE FROM vendors
            WHERE id = ?
        """;

        int count = jdbcTemplate.update(
                sql,
                id
        );

//        if (count == 0) {
//            throw new VendorNotFoundException(id);
//        }
    }

    @Override
    public List<VendorWithMaxFileDateTime> findVendorsWithLastFileTimestamp() {
        String sql = """
            SELECT
            v.id,
            v.title,
            MAX(vf.updated_at) AS latest_updated_at
            FROM vendors v
            LEFT JOIN vendor_files vf ON v.id = vf.vendor_id
            GROUP BY v.id, v.title;
        """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new VendorWithMaxFileDateTime(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getObject("latest_updated_at", OffsetDateTime.class) == null ? OffsetDateTime.MIN : rs.getObject("latest_updated_at", OffsetDateTime.class)
                )
        );
    }



}
