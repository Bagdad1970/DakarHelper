package io.github.bagdad.dakarhelperservice.repository;

import io.github.bagdad.dakarhelperservice.DakarHelperTestConfiguration;
import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
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
        return Product.builder()
                .vendorId(1L)
                .name("name1")
                .prices(Map.of("storage", BigDecimal.valueOf(10.00)))
                .minPrice(BigDecimal.valueOf(10.00))
                .quantities(Map.of("storage", 10))
                .totalQuantity(10)
                .build();
    }

    @Test
    void Saving_excel_products_must_save_and_return_them() {
        // arrange
        Product product1 = createExcelProductForTesting();
        Product product2 = createExcelProductForTesting();
        List<Product> products = List.of(product1, product2);

        repository.saveAll(products);

        // act
        List<Product> found = repository.query(new ProductQuery());

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
        List<Product> found = repository.query(new ProductQuery());

        // assert
        assertThat(found).isNotEmpty();
        assertThat(found).isEqualTo(products);
    }

    @Test
    void Deleting_excel_products_by_vendor_file_id_must_delete_it() {
        Product product1 = Product.builder()
                .vendorId(1L)
                .build();

        Product product2 = Product.builder()
                .vendorId(1L)
                .build();

        Product product3 = Product.builder()
                .vendorId(2L)
                .build();

        List<Product> products = List.of(product1, product2, product3);

        repository.saveAll(products);

        repository.deleteByVendorId(1L);

        List<Product> found = repository.query(new ProductQuery());

        List<Product> expected = List.of(product3);

        assertThat(found).isNotEmpty();
        assertThat(found).isEqualTo(expected);
    }

    @Test
    void Query_to_excel_products() {
        Product product1 = Product.builder()
                .vendorId(1L)
                .name("Hankook 255/40R22")
                .prices(Map.of("опт", BigDecimal.valueOf(10.00), "розница", BigDecimal.valueOf(15.00)))
                .minPrice(BigDecimal.valueOf(10.00))
                .quantities(Map.of("склад1", 3, "склад2", 2))
                .totalQuantity(5)
                .build();

        Product product2 = Product.builder()
                .vendorId(2L)
                .name("Hankook 255/40R22")
                .prices(Map.of("опт", BigDecimal.valueOf(18.00), "розница", BigDecimal.valueOf(25.00)))
                .minPrice(BigDecimal.valueOf(18.00))
                .quantities(Map.of("склад1", 7))
                .totalQuantity(7)
                .build();

        Product product3 = Product.builder()
                .vendorId(3L)
                .name("Nokian 185/60R15")
                .prices(Map.of("опт", BigDecimal.valueOf(19.00), "розница", BigDecimal.valueOf(30.00)))
                .minPrice(BigDecimal.valueOf(19.00))
                .quantities(Map.of("склад1", 4, "склад2", 10))
                .totalQuantity(14)
                .build();

        List<Product> products = List.of(product1, product2, product3);

        repository.saveAll(products);

        ProductQuery query = ProductQuery.builder()
                .vendorIds(List.of(1L, 2L))
                .name("Hankook")
                .price(BigDecimal.valueOf(18.00))
                .quantity(5)
                .build();

        List<Product> found = repository.query(query);

        List<Product> expected = List.of(product1, product2);

        assertThat(found).isNotEmpty();
        assertThat(found).isEqualTo(expected);
    }
    
}
