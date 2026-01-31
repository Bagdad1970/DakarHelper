package io.github.bagdad.dakarhelperservice.repository.mapper;

import io.github.bagdad.dakarhelperservice.model.ExcelHeaderSubcategory;
import io.github.bagdad.models.excelparser.Category;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ExcelHeaderSubcategoryMapper implements RowMapper<ExcelHeaderSubcategory> {

    @Override
    public ExcelHeaderSubcategory mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExcelHeaderSubcategory excelHeaderSubcategory = new ExcelHeaderSubcategory();
        excelHeaderSubcategory.setId(rs.getLong("id"));
        excelHeaderSubcategory.setSubcategoryName(rs.getString("subcategory_name"));

        return excelHeaderSubcategory;
    }

}
