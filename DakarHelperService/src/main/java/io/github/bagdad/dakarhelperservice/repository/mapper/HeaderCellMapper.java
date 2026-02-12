package io.github.bagdad.dakarhelperservice.repository.mapper;

import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HeaderCellMapper implements RowMapper<HeaderCell> {

    @Override
    public HeaderCell mapRow(ResultSet rs, int rowNum) throws SQLException {
        HeaderCell headerCell = new HeaderCell();
        headerCell.setId(rs.getLong("id"));
        headerCell.setSubcategoryId(rs.getLong("subcategory_id"));
        headerCell.setOriginalName(rs.getString("original_name"));
        headerCell.setNormalizedName(rs.getString("normalized_name"));

        String categoryStr = rs.getString("category");
        headerCell.setCategory(categoryStr != null ? Category.valueOf(categoryStr.trim().toUpperCase()) : null);

        String cellStatusStr = rs.getString("cell_status");
        headerCell.setCellStatus(cellStatusStr != null ? CellStatus.valueOf(cellStatusStr.trim().toUpperCase()) : null);

        return headerCell;
    }

}