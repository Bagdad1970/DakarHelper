package io.github.bagdad.dakarhelperservice.service;

import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.dakarhelperservice.repository.implementation.ProductRepositoryImpl;
import io.github.bagdad.dakarhelperservice.service.implementation.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ProductService {

    @Mock
    private ProductRepositoryImpl repository;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void saveAll() {
        // assert
        Product product1 = Product.builder()
                .id("1")
                .vendorId(1L)
                .name("Hankook")
                .prices(Map.of("price", BigDecimal.valueOf(10.00)))
                .quantities(Map.of("count", 10))
                .build();

        Product product2 = Product.builder()
                .id("2")
                .vendorId(2L)
                .name("Michelin")
                .prices(Map.of("price", BigDecimal.valueOf(20.00)))
                .quantities(Map.of("count", 5))
                .build();

        List<Product> products = List.of(product1, product2);

        // act
        service.saveAll(products);

        // assert
        Mockito.verify(repository, times(1))
                .saveAll(products);
    }

    @Test
    void deleteByVendorId() {
        service.deleteByVendorId(1L);

        Mockito.verify(repository, times(1))
                .deleteByVendorId(1L);
    }

    @Test
    void query() {
        ProductQuery productQuery = ProductQuery.builder()
                .vendorIds(List.of(1L, 2L))
                .name("Hankook")
                .price(BigDecimal.valueOf(15))
                .quantity(5)
                .build();

        service.query(productQuery);

        Mockito.verify(repository, times(1))
                .query(productQuery);
    }

}
