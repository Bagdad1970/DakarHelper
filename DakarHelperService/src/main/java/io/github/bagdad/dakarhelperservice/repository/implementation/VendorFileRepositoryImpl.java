package io.github.bagdad.dakarhelperservice.repository.implementation;

import io.github.bagdad.dakarhelperservice.exception.VendorFileNotFoundException;
import io.github.bagdad.dakarhelperservice.model.VendorFile;
import io.github.bagdad.dakarhelperservice.repository.interfaces.VendorFileRepository;
import io.github.bagdad.dakarhelperservice.repository.mapper.VendorFileMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class VendorFileRepositoryImpl implements VendorFileRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final VendorFileMapper VENDOR_FILE_MAPPER = new VendorFileMapper();

    public VendorFileRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public VendorFile save(VendorFile vendorFile) {
        String sql = """
        INSERT INTO vendor_files (
            vendor_id,
            filepath,
            file_status,
            created_at,
            updated_at
        ) VALUES (?, ?, ?, ?, ?)
        RETURNING
            id,
            vendor_id,
            filepath,
            file_status,
            created_at,
            updated_at
        """;

        return jdbcTemplate.queryForObject(
                sql,
                VENDOR_FILE_MAPPER,
                vendorFile.getVendorId(),
                vendorFile.getFilepath(),
                vendorFile.getFileStatus().name(),
                vendorFile.getCreatedAt(),
                vendorFile.getUpdatedAt()
        );
    }


    @Override
    public void batchInsert(List<VendorFile> vendorFiles) {
        String sql = """
        INSERT INTO vendor_files (
            vendor_id,
            filepath,
            file_status,
            created_at,
            updated_at
        ) VALUES (?, ?, ?, ?, ?)
        """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                VendorFile vendorFile = vendorFiles.get(i);
                ps.setLong(1, vendorFile.getVendorId());
                ps.setString(2, vendorFile.getFilepath());
                ps.setString(3, vendorFile.getFileStatus() != null ? vendorFile.getFileStatus().name() : null);
                ps.setObject(4, vendorFile.getCreatedAt());
                ps.setObject(5, vendorFile.getUpdatedAt());
            }

            @Override
            public int getBatchSize() {
                return vendorFiles.size();
            }
        });
    }

    @Override
    public VendorFile update(VendorFile vendorFile) {
        String sql = """
            UPDATE vendor_files SET
                vendor_id = ?,
                filepath = ?,
                file_status = ?,
                updated_at = ?
            WHERE id = ?
            RETURNING
                id,
                vendor_id,
                filepath,
                file_status,
                created_at,
                updated_at
        """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    VENDOR_FILE_MAPPER,
                    vendorFile.getVendorId(),
                    vendorFile.getFilepath(),
                    vendorFile.getFileStatus().name(),
                    vendorFile.getUpdatedAt(),
                    vendorFile.getId()
            );
        }
        catch (EmptyResultDataAccessException e) {
            throw new VendorFileNotFoundException(vendorFile.getId());
        }
    }

    @Override
    public List<VendorFile> findAll() {
        String sql = """
            SELECT
                id,
                vendor_id,
                filepath,
                file_status,
                created_at,
                updated_at
            FROM vendor_files
        """;

        return jdbcTemplate.query(
                sql,
                VENDOR_FILE_MAPPER
        );
    }

    @Override
    public Optional<VendorFile> findById(Long id) {
        String sql = """
            SELECT
                id,
                vendor_id,
                filepath,
                file_status,
                created_at,
                updated_at
            FROM vendor_files
            WHERE id = ?
        """;

        try {
            VendorFile vendorFile = jdbcTemplate.queryForObject(
                    sql,
                    VENDOR_FILE_MAPPER,
                    id
            );
            return Optional.ofNullable(vendorFile);
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
