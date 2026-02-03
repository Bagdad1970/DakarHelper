package io.github.bagdad.dakarhelperservice.repository.mapper;

import io.github.bagdad.dakarhelperservice.model.HeaderCellWithSubcategory;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HeaderCellWithSubcategoryMapper implements RowMapper<HeaderCellWithSubcategory> {

    @Override
    public HeaderCellWithSubcategory mapRow(ResultSet rs, int rowNum) throws SQLException {
        HeaderCellWithSubcategory dto = new HeaderCellWithSubcategory();

        dto.setId(rs.getLong("id"));
        dto.setExcelHeaderSubcategoryId(rs.getLong("subcategory_id"));
        dto.setOriginName(rs.getString("original_name"));
        dto.setNormalizedName(rs.getString("normalized_name"));

        String categoryStr = rs.getString("category");
        dto.setCategory(categoryStr != null ? Category.valueOf(categoryStr.trim().toUpperCase()) : null);

        String cellStatusStr = rs.getString("cell_status");
        dto.setCellStatus(cellStatusStr != null ? CellStatus.valueOf(cellStatusStr.trim().toUpperCase()) : null);

        Long subId = rs.getObject("sub_id", Long.class);
        dto.setSubcategoryId(subId);

        dto.setSubcategoryName(rs.getString("name"));

        return dto;
    }
}