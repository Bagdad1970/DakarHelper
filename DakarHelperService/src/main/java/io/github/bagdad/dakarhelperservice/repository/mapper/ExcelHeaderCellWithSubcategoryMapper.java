package io.github.bagdad.dakarhelperservice.repository.mapper;

import io.github.bagdad.dakarhelperservice.model.ExcelHeaderCell;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderCellWithSubcategory;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExcelHeaderCellWithSubcategoryMapper implements RowMapper<ExcelHeaderCellWithSubcategory> {

    @Override
    public ExcelHeaderCellWithSubcategory mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExcelHeaderCellWithSubcategory dto = new ExcelHeaderCellWithSubcategory();

        dto.setId(rs.getLong("id"));
        dto.setExcelHeaderSubcategoryId(rs.getLong("excel_header_subcategory_id"));
        dto.setOriginName(rs.getString("origin_name"));
        dto.setNormalizedName(rs.getString("normalized_name"));

        String categoryStr = rs.getString("category");
        dto.setCategory(categoryStr != null ? Category.valueOf(categoryStr.trim().toUpperCase()) : null);

        String cellStatusStr = rs.getString("cell_status");
        dto.setCellStatus(cellStatusStr != null ? CellStatus.valueOf(cellStatusStr.trim().toUpperCase()) : null);

        Long subId = rs.getObject("sub_id", Long.class);
        dto.setSubcategoryId(subId);

        dto.setSubcategoryName(rs.getString("subcategory_name"));

        return dto;
    }
}