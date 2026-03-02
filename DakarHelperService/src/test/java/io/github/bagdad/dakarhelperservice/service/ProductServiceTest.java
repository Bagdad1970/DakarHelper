package io.github.bagdad.dakarhelperservice.service;

import io.github.bagdad.dakarhelperservice.helper.ProductHelper;
import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.repository.implementation.ProductRepositoryImpl;
import io.github.bagdad.dakarhelperservice.service.implementation.ProductServiceImpl;
import io.github.bagdad.dakarhelperservice.service.implementation.VendorServiceImpl;
import io.github.bagdad.models.response.product.Pagination;
import io.github.bagdad.models.response.product.ProductQueryItem;
import io.github.bagdad.models.response.product.ProductQueryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepositoryImpl repository;

    @Mock
    private VendorServiceImpl vendorService;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void saveAll() {
        // arrange
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
        // arrange
        ProductQuery productQuery = ProductQuery.builder()
                .vendorIds(List.of(1L, 2L))
                .name("Hankook")
                .price(BigDecimal.valueOf(15))
                .quantity(5)
                .build();

        List<Vendor> vendors = List.of(
                new Vendor(1L, "vendor1"),
                new Vendor(2L, "vendor2")
        );

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

        Product product3 = Product.builder()
                .id("3")
                .vendorId(2L)
                .name("Goodyear")
                .prices(Map.of("price", BigDecimal.valueOf(25.00)))
                .quantities(Map.of("count", 3))
                .build();

        Pageable pageable = PageRequest.of(0, 20);

        List<Product> products = List.of(product1, product2, product3);
        Page<Product> productPage = new PageImpl<Product>(products);

        Mockito.when(repository.query(productQuery, pageable))
                        .thenReturn(productPage);

        Mockito.when(vendorService.findAll())
                .thenReturn(vendors);

        // act
        ProductQueryResponse res = service.query(productQuery);

        //assert
        List<ProductQueryItem> expectedProductQueryItems = products.stream()
                .map(product -> ProductHelper.mapToProductQueryItem(product, vendors, productQuery.getMargin()))
                .toList();
        Pagination expectedPagination = Pagination.builder()
                .pageIndex(0)
                .totalRecords((long) products.size())
                .totalPages(1)
                .build();
        ProductQueryResponse expected = new ProductQueryResponse();
        expected.setProductData(expectedProductQueryItems);
        expected.setPagination(expectedPagination);

        assertThat(res).isEqualTo(expected);

        Mockito.verify(repository, times(1))
                .query(productQuery, pageable);
        Mockito.verify(vendorService, times(1))
                .findAll();
    }

}
