package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.model.ExcelProduct;
import io.github.bagdad.dakarhelperservice.model.ExcelProductQuery;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ExcelProductRepository;
import io.github.bagdad.dakarhelperservice.service.interfaces.ExcelProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExcelProductServiceImpl implements ExcelProductService {

    private final ExcelProductRepository repository;

    public ExcelProductServiceImpl(ExcelProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveAll(List<ExcelProduct> excelProducts) {
        repository.saveAll(excelProducts);
    }

    @Override
    public List<ExcelProduct> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ExcelProduct> query(ExcelProductQuery query) {
        return List.of();
    }

}
