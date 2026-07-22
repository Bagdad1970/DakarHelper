package io.github.bagdad.dakarhelperservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.model.HeaderCellWithSubcategory;
import io.github.bagdad.dakarhelperservice.service.implementation.HeaderCellServiceImpl;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.request.headercell.HeaderCellCreateRequest;
import io.github.bagdad.models.request.headercell.HeaderCellUpdateRequest;
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

@WebMvcTest(HeaderCellController.class)
public class HeaderCellControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HeaderCellServiceImpl service;

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
        HeaderCellCreateRequest request = new HeaderCellCreateRequest();
        request.setSubcategoryId(1L);
        request.setOriginalName("original_name");
        request.setCategory(Category.NAME);
        request.setIsProcessing(true);

        HeaderCell saved = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("original_name")
                .category(Category.NAME)
                .isProcessing(true)
                .build();

        Mockito.when(service.create(Mockito.any(HeaderCell.class)))
                .thenReturn(saved);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/header-cells")
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
//        HeaderCellUpdateRequest request = new HeaderCellUpdateRequest();
//        request.setId(1L);
//        request.setSubcategoryId(1L);
//        request.setOriginalName("original_name");
//        request.setCategory(Category.NAME);
//        request.setIsProcessing(true);
//
//        HeaderCell updated = HeaderCell.builder()
//                .id(1L)
//                .subcategoryId(1L)
//                .originalName("updated_original_name")
//                .category(Category.NAME)
//                .isProcessing(true)
//                .build();
//
//        Mockito.when(service.update(updated))
//                .thenReturn(updated);
//
//        // act & assert
//        mockMvc.perform(MockMvcRequestBuilders
//                        .put("/api/header-cells/1")
//                        .content(asJsonString(request))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().json(asJsonString(updated)));
//    }

    @Test
    void Finding_by_id_existing_header_cell_must_return_it() throws Exception {
        // arrange
        HeaderCell headerCell = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("original_name")
                .category(Category.NAME)
                .isProcessing(true)
                .build();

        Mockito.when(service.findById(1L))
                .thenReturn(Optional.of(headerCell));

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/header-cells/1")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(headerCell)));
        Mockito.verify(service, times(1))
                .findById(1L);
    }

    @Test
    void Finding_all_header_cells_must_return_them() throws Exception {
        // arrange
        HeaderCell headerCell1 = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("original_name1")
                .category(Category.NAME)
                .isProcessing(true)
                .build();

        HeaderCell headerCell2 = HeaderCell.builder()
                .id(2L)
                .subcategoryId(2L)
                .originalName("original_name2")
                .category(Category.NAME)
                .isProcessing(true)
                .build();

        List<HeaderCell> headerCells = List.of(headerCell1, headerCell2);

        Mockito.when(service.findAll())
                .thenReturn(headerCells);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/header-cells")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(headerCells)));
        Mockito.verify(service, times(1))
                .findAll();
    }

    @Test
    void Finding_all_header_cells_with_subcategory_must_return_them() throws Exception {
        // arrange
        HeaderCellWithSubcategory headerCellWithSubcategory1 = HeaderCellWithSubcategory.builder()
                .id(1L)
                .subcategoryName("subcategory_name1")
                .originalName("original_name1")
                .category(Category.NAME)
                .isProcessing(true)
                .build();

        HeaderCellWithSubcategory headerCellWithSubcategory2 = HeaderCellWithSubcategory.builder()
                .id(2L)
                .subcategoryName("subcategory_name2")
                .originalName("original_name2")
                .category(Category.NAME)
                .isProcessing(true)
                .build();

        List<HeaderCellWithSubcategory> headerCellWithSubcategories = List.of(headerCellWithSubcategory1, headerCellWithSubcategory2);

        Mockito.when(service.findAllWithSubcategory())
                .thenReturn(headerCellWithSubcategories);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/header-cells/with-subcategory")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(headerCellWithSubcategories)));
        Mockito.verify(service, times(1))
                .findAll();
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/header-cells/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        Mockito.verify(service)
                .deleteById(1L);
    }
    
}
