package io.github.bagdad.dakarhelperservice.service.interfaces;

import io.github.bagdad.dakarhelperservice.model.ExcelProduct;
import io.github.bagdad.dakarhelperservice.model.ExcelProductQuery;

import java.util.List;

public interface ExcelProductService {

    void saveAll(List<ExcelProduct> excelProducts);

    List<ExcelProduct> findAll();

    List<ExcelProduct> query(ExcelProductQuery query);

}
