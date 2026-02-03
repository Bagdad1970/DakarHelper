package io.github.bagdad.dakarhelperservice.repository;

import io.github.bagdad.dakarhelperservice.DakarHelperTestConfiguration;
import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.repository.implementation.ProductRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ContextConfiguration(classes = DakarHelperTestConfiguration.class)
@Import(ProductRepositoryImpl.class)
@Testcontainers
public class ProductRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProductRepositoryImpl repository;

    @AfterEach
    void cleanUp() {
        mongoTemplate.dropCollection(Product.class);
    }

    private static Product createExcelProductForTesting() {
        Product product = new Product();
        product.setVendorFileId(1L);
        product.setNames(Map.of("name", "name1"));
        product.setPrices(Map.of("storage", BigDecimal.valueOf(10.00)));
        product.setQuantities(Map.of("storage", 10));

        return product;
    }

    @Test
    void Saving_excel_products_must_save_and_return_them() {
        // arrange
        Product product1 = createExcelProductForTesting();
        Product product2 = createExcelProductForTesting();
        List<Product> products = List.of(product1, product2);

        repository.saveAll(products);

        // act
        List<Product> found = repository.findAll();

        // assert
        assertThat(found).isNotEmpty();
        assertThat(found).isEqualTo(products);
    }

    @Test
    void Finding_all_excel_products_must_return_existing_entities() {
        // arrange
        Product product1 = createExcelProductForTesting();
        Product product2 = createExcelProductForTesting();
        List<Product> products = List.of(product1, product2);

        repository.saveAll(products);

        // act
        List<Product> found = repository.findAll();

        // assert
        assertThat(found).isNotEmpty();
        assertThat(found).isEqualTo(products);
    }

    @Test
    void Deleting_excel_products_by_vendor_file_id_must_delete_it() {
        Product product1 = Product.builder()
                .vendorFileId(1L)
                .build();

        Product product2 = Product.builder()
                .vendorFileId(1L)
                .build();

        Product product3 = Product.builder()
                .vendorFileId(2L)
                .build();

        List<Product> products = List.of(product1, product2, product3);

        repository.saveAll(products);

        repository.deleteByVendorFileId(1L);

        List<Product> found = repository.findAll();

        List<Product> expected = List.of(product3);

        assertThat(found).isNotEmpty();
        assertThat(found).isEqualTo(expected);
    }
    
}
