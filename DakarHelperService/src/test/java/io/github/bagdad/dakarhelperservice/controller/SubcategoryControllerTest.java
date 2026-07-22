package io.github.bagdad.dakarhelperservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.dakarhelperservice.service.implementation.SubcategoryServiceImpl;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.request.subcategory.SubcategoryCreateRequest;
import io.github.bagdad.models.request.subcategory.SubcategoryUpdateRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubcategoryController.class)
public class SubcategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubcategoryServiceImpl service;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void create() throws  Exception {
        // arrange
        SubcategoryCreateRequest request = new SubcategoryCreateRequest();
        request.setCategory(Category.NAME);
        request.setName("name");

        Subcategory saved = Subcategory.builder()
                .id(1L)
                .category(Category.NAME)
                .name("updated_name")
                .build();

        Mockito.when(service.create(Mockito.any(Subcategory.class)))
                .thenReturn(saved);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/subcategories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(saved)));
    }

//    @Test
//    void update() throws Exception {
//        // arrange
//        SubcategoryUpdateRequest request = new SubcategoryUpdateRequest();
//        request.setId(1L);
//        request.setCategory(Category.PRICE);
//        request.setName("updated_name");
//
//        Subcategory updated = Subcategory.builder()
//                .id(1L)
//                .category(Category.NAME)
//                .name("updated_name")
//                .build();
//
//        Mockito.when(service.update(Mockito.any(Subcategory.class)))
//                .thenReturn(updated);
//
//        // act & assert
//        mockMvc.perform(MockMvcRequestBuilders
//                        .put("/api/subcategories/1")
//                        .content(asJsonString(request))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().json(asJsonString(updated)));
//    }

    @Test
    void Finding_by_id_existing_subcategory_must_return_it() throws Exception {
        // arrange
        Subcategory subcategory = Subcategory.builder()
                .id(1L)
                .category(Category.NAME)
                .name("name")
                .build();

        Mockito.when(service.findById(1L))
                .thenReturn(Optional.of(subcategory));

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/subcategories/1")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(subcategory)));
        Mockito.verify(service, times(1))
                .findById(1L);
    }

    @Test
    void findAll() throws Exception {
        // arrange
        Subcategory subcategory1 = Subcategory.builder()
                .id(1L)
                .category(Category.NAME)
                .name("name1")
                .build();

        Subcategory subcategory2 = Subcategory.builder()
                .id(2L)
                .category(Category.NAME)
                .name("name2")
                .build();

        List<Subcategory> subcategories = List.of(subcategory1, subcategory2);

        Mockito.when(service.findAll())
                .thenReturn(subcategories);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/subcategories")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(subcategories)));
        Mockito.verify(service, times(1))
                .findAll();
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/subcategories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        Mockito.verify(service)
                .deleteById(1L);
    }

}
