package io.github.bagdad.dakarhelperservice.repository.mapper;

import io.github.bagdad.dakarhelperservice.model.FileStatus;
import io.github.bagdad.dakarhelperservice.model.VendorFile;
import org.springframework.jdbc.core.RowMapper;

import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class VendorFileMapper implements RowMapper<VendorFile> {

    @Override
    public VendorFile mapRow(ResultSet rs, int rowNum) throws SQLException {
        VendorFile vendorFile = new VendorFile();
        vendorFile.setId(rs.getLong("id"));
        vendorFile.setVendorId(rs.getLong("vendor_id"));
        vendorFile.setFilepath(Path.of(rs.getString("filepath")));

        String fileStatusStr = rs.getString("file_status");
        vendorFile.setFileStatus(fileStatusStr != null ? FileStatus.valueOf(fileStatusStr.trim().toUpperCase()) : null);

        vendorFile.setUpdatedAt(rs.getObject("updated_at", OffsetDateTime.class));

        return vendorFile;
    }

}
