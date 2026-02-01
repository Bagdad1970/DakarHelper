package io.github.bagdad.dakarhelperservice.repository.mapper;

import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.models.excelparser.Category;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SubcategoryMapper implements RowMapper<Subcategory> {

    @Override
    public Subcategory mapRow(ResultSet rs, int rowNum) throws SQLException {
        Subcategory subcategory = new Subcategory();
        subcategory.setId(rs.getLong("id"));

        String categoryStr = rs.getString("category");
        subcategory.setCategory(categoryStr != null ? Category.valueOf(categoryStr.trim().toUpperCase()) : null);

        subcategory.setName(rs.getString("name"));

        return subcategory;
    }

}
