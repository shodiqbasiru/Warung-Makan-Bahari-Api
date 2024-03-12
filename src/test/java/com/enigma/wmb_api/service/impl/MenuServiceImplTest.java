package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.MenuRequest;
import com.enigma.wmb_api.dto.request.PaginationMenuRequest;
import com.enigma.wmb_api.dto.response.MenuResponse;
import com.enigma.wmb_api.entity.Image;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.MenuRepository;
import com.enigma.wmb_api.service.ImageService;
import com.enigma.wmb_api.service.MenuService;
import com.enigma.wmb_api.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private ValidationUtil validation;
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuServiceImpl(menuRepository, validation, imageService);
    }
    @Test
    void createCreatesNewMenuWhenValidRequest() {
        byte[] content = "test content".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile mockImage = new MockMultipartFile("image", "test.jpg", "image/jpg", content);

        MenuRequest request = new MenuRequest();
        request.setMenuName("Test Menu");
        request.setPrice(10);
        request.setImage(mockImage);

        Image image = new Image();
        image.setId("1");
        image.setName("testImage");

        Menu menu = new Menu();
        menu.setMenuName(request.getMenuName());
        menu.setPrice(request.getPrice());
        menu.setImage(image);

        when(imageService.create(Mockito.any())).thenReturn(image);
        when(menuRepository.saveAndFlush(Mockito.any(Menu.class))).thenReturn(menu);

        MenuResponse response = menuService.create(request);

        assertNotNull(response);
        assertEquals("Test Menu", response.getMenuName());
        assertEquals(10, response.getPrice());
    }

    @Test
    void getOneByIdReturnsMenuResponseWhenMenuExists() {
        Image image = new Image();
        image.setId("1");
        image.setName("testImage");

        String id = "1";
        Menu menu = new Menu();
        menu.setId(id);
        menu.setMenuName("Test Menu");
        menu.setPrice(10);
        menu.setImage(image);

        when(menuRepository.findById(id)).thenReturn(Optional.of(menu));

        MenuResponse response = menuService.getOneById(id);

        assertNotNull(response);
        assertEquals("Test Menu", response.getMenuName());
        assertEquals(10, response.getPrice());
    }

    @Test
    void getOneByIdThrowsResponseStatusExceptionWhenMenuDoesNotExist() {
        String id = "1";

        when(menuRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> menuService.getOneById(id));
    }

    @Test
    void getByIdReturnsMenuWhenMenuExists() {
        String id = "1";
        Menu menu = new Menu();
        menu.setId(id);
        menu.setMenuName("Test Menu");
        menu.setPrice(10);

        when(menuRepository.findById(id)).thenReturn(Optional.of(menu));

        Menu returnedMenu = menuService.getById(id);

        assertNotNull(returnedMenu);
        assertEquals("Test Menu", returnedMenu.getMenuName());
        assertEquals(10, returnedMenu.getPrice());
    }

    @Test
    void getByIdThrowsResponseStatusExceptionWhenMenuDoesNotExist() {
        String id = "1";

        when(menuRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> menuService.getById(id));
    }
    @Test
    void getAllReturnsPageOfMenusWhenValidRequest() {
        PaginationMenuRequest request = new PaginationMenuRequest();
        request.setPage(1);
        request.setSize(10);
        request.setSortBy("menuName");
        request.setDirection("ASC");

        Menu menu = new Menu();
        menu.setMenuName("Test Menu");
        menu.setPrice(10);

        Page<Menu> page = new PageImpl<>(Collections.singletonList(menu));

        when(menuRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(page);

        Page<Menu> returnedPage = menuService.getAll(request);

        assertNotNull(returnedPage);
        assertEquals(1, returnedPage.getTotalElements());
        assertEquals("Test Menu", returnedPage.getContent().get(0).getMenuName());
    }

    @Test
    void updateUpdatesMenuWhenValidRequestAndImageNotNull() {
        byte[] content = "test content".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile mockImage = new MockMultipartFile("image", "test.jpg", "image/jpg", content);

        MenuRequest request = new MenuRequest();
        request.setId("1");
        request.setMenuName("Updated Menu");
        request.setPrice(20);
        request.setImage(mockImage);

        Image image = new Image();
        image.setId("1");
        image.setName("testImage");

        Menu menu = new Menu();
        menu.setId(request.getId());
        menu.setMenuName(request.getMenuName());
        menu.setPrice(request.getPrice());
        menu.setImage(image);

        when(imageService.create(Mockito.any())).thenReturn(image);
        when(menuRepository.findById(request.getId())).thenReturn(Optional.of(menu));
        when(menuRepository.saveAndFlush(Mockito.any(Menu.class))).thenReturn(menu);

        MenuResponse response = menuService.update(request);

        assertNotNull(response);
        assertEquals("Updated Menu", response.getMenuName());
        assertEquals(20, response.getPrice());
    }

    @Test
    void updateUpdatesMenuWhenValidRequestAndImageNull() {
        MenuRequest request = new MenuRequest();
        request.setId("1");
        request.setMenuName("Updated Menu");
        request.setPrice(20);

        Image image = new Image();
        image.setId("1");
        image.setName("testImage");

        Menu menu = new Menu();
        menu.setId(request.getId());
        menu.setMenuName(request.getMenuName());
        menu.setPrice(request.getPrice());
        menu.setImage(image);

        when(menuRepository.findById(request.getId())).thenReturn(Optional.of(menu));
        when(menuRepository.saveAndFlush(Mockito.any(Menu.class))).thenReturn(menu);

        MenuResponse response = menuService.update(request);

        assertNotNull(response);
        assertEquals("Updated Menu", response.getMenuName());
        assertEquals(20, response.getPrice());
    }

    @Test
    void updateThrowsResponseStatusExceptionWhenMenuDoesNotExist() {
        MenuRequest request = new MenuRequest();
        request.setId("1");
        request.setMenuName("Updated Menu");
        request.setPrice(20);

        when(menuRepository.findById(request.getId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> menuService.update(request));
    }

    @Test
    void deleteDeletesMenuWhenMenuExists() {
        String id = "1";
        Menu menu = new Menu();
        menu.setId(id);
        menu.setMenuName("Test Menu");
        menu.setPrice(10);

        when(menuRepository.findById(id)).thenReturn(Optional.of(menu));
        doNothing().when(menuRepository).delete(menu);

        menuService.delete(id);

        Mockito.verify(menuRepository, Mockito.times(1)).delete(menu);
    }

    @Test
    void deleteThrowsResponseStatusExceptionWhenMenuDoesNotExist() {
        String id = "1";

        when(menuRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> menuService.delete(id));
    }
}