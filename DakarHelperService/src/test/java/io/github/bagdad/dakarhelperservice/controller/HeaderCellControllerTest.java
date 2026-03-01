package io.github.bagdad.dakarhelperservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.service.implementation.HeaderCellServiceImpl;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
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
        request.setNormalizedName("normalized_name");
        request.setCategory(Category.NAME);
        request.setCellStatus(CellStatus.PROCESSED);

        HeaderCell saved = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("original_name")
                .normalizedName("normalized_name")
                .category(Category.NAME)
                .cellStatus(CellStatus.PROCESSED)
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

    @Test
    void update() throws Exception {
        // arrange
        HeaderCellUpdateRequest request = new HeaderCellUpdateRequest();
        request.setId(1L);
        request.setSubcategoryId(1L);
        request.setOriginalName("original_name");
        request.setNormalizedName("normalized_name");
        request.setCategory(Category.NAME);
        request.setCellStatus(CellStatus.PROCESSED);

        HeaderCell updated = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("updated_original_name")
                .normalizedName("updated_normalized_name")
                .category(Category.NAME)
                .cellStatus(CellStatus.PROCESSED)
                .build();

        Mockito.when(service.update(Mockito.any(HeaderCell.class)))
                .thenReturn(updated);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/header-cells/1")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(updated)));
    }

    @Test
    void Finding_by_id_existing_header_cell_must_return_it() throws Exception {
        // arrange
        HeaderCell headerCell = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("original_name")
                .normalizedName("normalized_name")
                .category(Category.NAME)
                .cellStatus(CellStatus.PROCESSED)
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
    void findAll() throws Exception {
        // arrange
        HeaderCell headerCell1 = HeaderCell.builder()
                .id(1L)
                .subcategoryId(1L)
                .originalName("original_name1")
                .normalizedName("normalized_name1")
                .category(Category.NAME)
                .cellStatus(CellStatus.PROCESSED)
                .build();

        HeaderCell headerCell2 = HeaderCell.builder()
                .id(2L)
                .subcategoryId(2L)
                .originalName("original_name2")
                .normalizedName("normalized_name2")
                .category(Category.NAME)
                .cellStatus(CellStatus.PROCESSED)
                .build();

        List<HeaderCell> subcategories = List.of(headerCell1, headerCell2);

        Mockito.when(service.findAll())
                .thenReturn(subcategories);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/header-cells")
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
