package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.dto.request.MenuRequest;
import com.enigma.wmb_api.dto.request.PaginationMenuRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.MenuResponse;
import com.enigma.wmb_api.entity.Image;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.service.MenuService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class MenuControllerTest {
    @MockBean
    private MenuService menuService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testCreate_shouldReturn201_whenCreateMenu() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "image.jpeg", MediaType.IMAGE_JPEG_VALUE, "hello".getBytes());
        MenuRequest mockMenu = MenuRequest.builder()
                .id("1")
                .menuName("menu")
                .price(1000)
                .build();

        String jsonMenu = objectMapper.writeValueAsString(mockMenu);

        MenuResponse mockResponse = MenuResponse.builder()
                .id(mockMenu.getId())
                .menuName(mockMenu.getMenuName())
                .price(mockMenu.getPrice())
                .build();
        when(menuService.create(any(MenuRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(
                        MockMvcRequestBuilders.post(RouteApi.MENU_PATH)
                                .param("image", Arrays.toString(file.getBytes())) // change this line
                                .param("menu", jsonMenu)
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isCreated());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetAll_shouldReturn200_whenGetAllMenu() throws Exception {
        Integer page = 0;
        Integer size = 10;
        String sortBy = "name";
        String direction = "asc";
        String name = "kopi";
        Integer minPrice = 1000;
        Integer maxPrice = 2000;
        PaginationMenuRequest payload = PaginationMenuRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .menuName(name)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .build();

        Image image1 = Image.builder()
                .id("image_1")
                .name("image 1")
                .path("path image 1")
                .size(1000L)
                .build();
        Image image2 = Image.builder()
                .id("image_2")
                .name("image 2")
                .path("path image 2")
                .size(2000L)
                .build();
        List<Menu> mockCustomerResponse = List.of(
                new Menu("1", "kopi", 4000, image1),
                new Menu("2", "teh", 4000, image2)
        );

        Pageable pageable = PageRequest.of(payload.getPage(), payload.getSize(), Sort.by(payload.getSortBy()));
        Page<Menu> menuPage = new PageImpl<>(mockCustomerResponse, pageable, mockCustomerResponse.size());

        when(menuService.getAll(any(PaginationMenuRequest.class))).thenReturn(menuPage);

        String stringJson = objectMapper.writeValueAsString(payload);
        mockMvc.perform(
                        MockMvcRequestBuilders.get(RouteApi.MENU_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stringJson)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<List<MenuResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response);
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Get all data successfully", response.getMessage());
                });
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testFindById_shouldReturn200_whenFindMenuById() throws Exception {
        Image image1 = Image.builder()
                .id("image_1")
                .name("image 1")
                .path("path image 1")
                .size(1000L)
                .build();

        Menu menu = new Menu("1", "kopi", 4000, image1);

        when(menuService.getById(anyString())).thenReturn(menu);
        mockMvc.perform(
                        MockMvcRequestBuilders.get(RouteApi.MENU_PATH + "/" + menu.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<MenuResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response);
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Get data successfully", response.getMessage());
                }
        );
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testUpdate_shouldReturn201_whenUpdateMenu() {
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testDelete_shouldReturn200_whenDeleteMEnu() throws Exception {
        String id = "1";
        doNothing().when(menuService).delete(id);
        mockMvc.perform(
                        MockMvcRequestBuilders.delete(RouteApi.MENU_PATH + "/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    verify(menuService, times(1)).delete(id);
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Deleted data successfully", response.getMessage());
                });

    }
}