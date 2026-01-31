package io.github.bagdad.dakarhelperservice.repository.interfaces;

import io.github.bagdad.dakarhelperservice.model.ExcelProduct;
import io.github.bagdad.dakarhelperservice.model.ExcelProductQuery;

import java.util.List;

public interface ExcelProductRepository {

    void saveAll(List<ExcelProduct> excelProducts);

    List<ExcelProduct> findAll();

    List<ExcelProduct> query(ExcelProductQuery query);

}
