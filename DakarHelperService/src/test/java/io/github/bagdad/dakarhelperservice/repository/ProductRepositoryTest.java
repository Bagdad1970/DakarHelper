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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
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

    private static Product createProductForTesting() {
        return Product.builder()
                .vendorId(1L)
                .name("name1")
                .minPrice(BigDecimal.valueOf(10.00))
                .quantities(Map.of("storage", 10))
                .totalQuantity(10)
                .build();
    }

    private static List<Product> createProductsForQuery() {
        Product product1 = Product.builder()
                .vendorId(1L)
                .name("Hankook 210/32R21")
                .minPrice(BigDecimal.valueOf(10.00))
                .totalQuantity(5)
                .build();

        Product product2 = Product.builder()
                .vendorId(2L)
                .name("Hankook 255/40R22")
                .minPrice(BigDecimal.valueOf(20.00))
                .totalQuantity(7)
                .build();

        Product product3 = Product.builder()
                .vendorId(3L)
                .name("Michelin 223/27R18")
                .minPrice(BigDecimal.valueOf(18.00))
                .totalQuantity(10)
                .build();

        Product product4 = Product.builder()
                .vendorId(2L)
                .name("INFINITY 215/75")
                .minPrice(BigDecimal.valueOf(25.00))
                .totalQuantity(3)
                .build();

        return List.of(product1, product2, product3, product4);
    }

    @Test
    void Saving_products_must_save_and_return_them() {
        // arrange
        Product product1 = createProductForTesting();
        Product product2 = createProductForTesting();
        List<Product> products = List.of(product1, product2);

        repository.saveAll(products);

        // act
        List<Product> found = repository.findAll();

        // assert
        assertThat(found).isNotEmpty();
        assertThat(found).isEqualTo(products);
    }

    @Test
    void Finding_all_products_must_return_existing_entities() {
        // arrange
        Product product1 = createProductForTesting();
        Product product2 = createProductForTesting();
        List<Product> products = List.of(product1, product2);

        repository.saveAll(products);

        // act
        List<Product> found = repository.findAll();

        // assert
        assertThat(found).isNotEmpty();
        assertThat(found).isEqualTo(products);
    }

    @Test
    void Deleting_products_by_vendor_file_id_must_delete_it() {
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

        List<Product> found = repository.findAll();

        List<Product> expected = List.of(product3);

        assertThat(found).isNotEmpty();
        assertThat(found).isEqualTo(expected);
    }

    @Test
    void Querying_products_without_vendors_returns_empty_collection() {
        // arrange
        List<Product> products = createProductsForQuery();

        repository.saveAll(products);

        ProductQuery query = ProductQuery.builder().build();
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

        // act
        Page<Product> found = repository.query(query, pageable);

        // assert
        assertThat(found.getTotalElements()).isZero();
        assertThat(found.getTotalPages()).isZero();
        assertThat(found.getContent()).isEmpty();
    }

    @Test
    void Querying_products_with_pagination_returns_page_of_matching_products() {
        // arrange
        List<Product> products = createProductsForQuery();

        repository.saveAll(products);

        ProductQuery query = ProductQuery.builder()
                .vendorIds(List.of(1L, 2L, 3L))
                .build();
        Pageable pageable = PageRequest.of(0, 2);

        // act
        Page<Product> found = repository.query(query, pageable);

        // assert
        assertThat(found).isNotEmpty();
        assertThat(found.getContent()).isEqualTo(List.of(products.get(0), products.get(1)));
        assertThat(found.getTotalElements()).isEqualTo(products.size());
        assertThat(found.getTotalPages()).isEqualTo(2);
    }

    @Test
    void Querying_products_with_sorting_returns_sorted_and_matching_products() {
        // arrange
        List<Product> products = createProductsForQuery();

        repository.saveAll(products);

        LinkedHashMap<String, Integer> sortingConditions = new LinkedHashMap<>();
        sortingConditions.put("min_price", 1);
        sortingConditions.put("total_quantity", -1);
        ProductQuery query = ProductQuery.builder()
                .vendorIds(List.of(1L, 2L, 3L))
                .sortingConditions(sortingConditions)
                .build();
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

        // act
        Page<Product> found = repository.query(query, pageable);

        // assert
        assertThat(found.getContent()).isNotEmpty();
        assertThat(found.getContent()).isEqualTo(List.of(products.get(3), products.get(1), products.get(2), products.get(0)));
        assertThat(found.getTotalElements()).isEqualTo(products.size());
    }

    @Test
    void Querying_products_with_sorting_returns_page_of_sorted_and_matching_products() {
        // arrange
        List<Product> products = createProductsForQuery();

        repository.saveAll(products);

        LinkedHashMap<String, Integer> sortingConditions = new LinkedHashMap<>();
        sortingConditions.put("min_price", 1);
        sortingConditions.put("total_quantity", -1);
        ProductQuery query = ProductQuery.builder()
                .vendorIds(List.of(1L, 2L, 3L))
                .sortingConditions(sortingConditions)
                .build();
        Pageable pageable = PageRequest.of(0, 2);

        // act
        Page<Product> found = repository.query(query, pageable);

        // assert
        assertThat(found.getContent()).isNotEmpty();
        assertThat(found.getContent()).isEqualTo(List.of(products.get(3), products.get(1)));
        assertThat(found.getTotalElements()).isEqualTo(4);
    }
    
}
