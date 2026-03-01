package io.github.bagdad.dakarhelperservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.service.implementation.VendorServiceImpl;
import io.github.bagdad.models.request.vendor.VendorCreateRequest;
import io.github.bagdad.models.request.vendor.VendorUpdateRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VendorController.class)
public class VendorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VendorServiceImpl service;

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
        VendorCreateRequest request = new VendorCreateRequest();
        request.setTitle("title");

        Vendor saved = Vendor.builder()
                .id(1L)
                .title("title")
                .build();

        Mockito.when(service.create(Mockito.any(Vendor.class)))
                .thenReturn(saved);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/vendors")
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
        VendorUpdateRequest request = new VendorUpdateRequest();
        request.setId(1L);
        request.setTitle("updated_title");

        Vendor updated = Vendor.builder()
                .id(1L)
                .title("updated_title")
                .build();

        Mockito.when(service.update(Mockito.any(Vendor.class)))
                .thenReturn(updated);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/vendors/1")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(updated)));
    }

    @Test
    void Finding_by_id_existing_vendor_must_return_it() throws Exception {
        // arrange
        Vendor vendor = Vendor.builder()
                .id(1L)
                .title("title")
                .build();

        Mockito.when(service.findById(1L))
                .thenReturn(Optional.of(vendor));

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/vendors/1")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(vendor)));
        Mockito.verify(service, times(1))
                .findById(1L);
    }

    @Test
    void findAll() throws Exception {
        // arrange
        Vendor vendor1 = Vendor.builder()
                .id(1L)
                .title("title1")
                .build();

        Vendor vendor2 = Vendor.builder()
                .id(2L)
                .title("title2")
                .build();

        List<Vendor> vendors = List.of(vendor1, vendor2);

        Mockito.when(service.findAll())
                .thenReturn(vendors);

        // act & assert
        mockMvc.perform(get("/api/vendors")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(vendors)));
        Mockito.verify(service, times(1))
                .findAll();
    }

    @Test
    void Finding_existing_vendor_by_id_must_return_it() throws Exception {
        // arrange
        Vendor vendor1 = Vendor.builder()
                .id(1L)
                .title("title1")
                .build();

        Vendor vendor2 = Vendor.builder()
                .id(2L)
                .title("title2")
                .build();

        List<Vendor> vendors = List.of(vendor1, vendor2);

        Mockito.when(service.findAll())
                .thenReturn(vendors);

        // act & assert
        mockMvc.perform(get("/api/vendors")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(vendors)));
        Mockito.verify(service, times(1))
                .findAll();
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.delete("/api/vendors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        Mockito.verify(service)
                .deleteById(1L);
    }

}
