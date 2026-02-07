package io.github.bagdad.dakarhelperservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bagdad.dakarhelperservice.model.Product;
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

@WebMvcTest(ExcelProductController.class)
public class ExcelProductControllerTest {

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
    void findAll() throws Exception {
        // arrange
        Product product1 = Product.builder()
                .id("1")
                .vendorFileId(1L)
                .names(Map.of("name", "name1"))
                .prices(Map.of("price", BigDecimal.valueOf(10.00)))
                .quantities(Map.of("count", 10))
                .build();

        Product product2 = Product.builder()
                .id("2")
                .vendorFileId(2L)
                .names(Map.of("name", "name2"))
                .prices(Map.of("price", BigDecimal.valueOf(20.00)))
                .quantities(Map.of("count", 5))
                .build();

        List<Product> subcategories = List.of(product1, product2);

        Mockito.when(service.findAll())
                .thenReturn(subcategories);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/products")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(subcategories)));
        Mockito.verify(service, times(1))
                .findAll();
    }

}
