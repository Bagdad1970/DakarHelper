package io.github.bagdad.dakarhelperservice.repository.mapper;

import io.github.bagdad.dakarhelperservice.model.*;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExcelHeaderCellMapper implements RowMapper<ExcelHeaderCell> {

    @Override
    public ExcelHeaderCell mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExcelHeaderCell excelHeaderCell = new ExcelHeaderCell();
        excelHeaderCell.setId(rs.getLong("id"));
        excelHeaderCell.setExcelHeaderSubcategoryId(rs.getLong("excel_header_subcategory_id"));
        excelHeaderCell.setOriginName(rs.getString("origin_name"));
        excelHeaderCell.setNormalizedName(rs.getString("normalized_name"));

        String categoryStr = rs.getString("category");
        excelHeaderCell.setCategory(categoryStr != null ? Category.valueOf(categoryStr.trim().toUpperCase()) : null);

        String cellStatusStr = rs.getString("cell_status");
        excelHeaderCell.setCellStatus(cellStatusStr != null ? CellStatus.valueOf(cellStatusStr.trim().toUpperCase()) : null);

        return excelHeaderCell;
    }

}