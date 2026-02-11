package io.github.bagdad.dakarhelperservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.dakarhelperservice.service.implementation.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductServiceImpl service;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void Query_must_return_desired_products() throws Exception {
        Product product1 = Product.builder()
                .id("1")
                .vendorFileId(1L)
                .name("Hankook")
                .prices(Map.of("price", BigDecimal.valueOf(10.00)))
                .minPrice(BigDecimal.valueOf(10.00))
                .quantities(Map.of("count", 10))
                .totalQuantity(10)
                .build();

        Product product2 = Product.builder()
                .id("2")
                .vendorFileId(2L)
                .name("Michelin")
                .prices(Map.of("price", BigDecimal.valueOf(20.00)))
                .minPrice(BigDecimal.valueOf(20.00))
                .quantities(Map.of("count", 5))
                .totalQuantity(5)
                .build();

        Product product3 = Product.builder()
                .id("3")
                .vendorFileId(2L)
                .name("Hankook")
                .prices(Map.of("price", BigDecimal.valueOf(15.00)))
                .minPrice(BigDecimal.valueOf(15.00))
                .quantities(Map.of("count", 5))
                .totalQuantity(10)
                .build();

        ProductQuery productQuery = ProductQuery.builder()
                .vendorIds(List.of(1L, 2L))
                .name("Hankook")
                .price(BigDecimal.valueOf(15))
                .quantity(5)
                .build();

        List<Product> desiredProducts = List.of(product1, product3);

        Mockito.when(service.query(productQuery))
                .thenReturn(desiredProducts);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/products/query")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productQuery))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(desiredProducts)));
        Mockito.verify(service, times(1))
                .query(productQuery);
    }


}
