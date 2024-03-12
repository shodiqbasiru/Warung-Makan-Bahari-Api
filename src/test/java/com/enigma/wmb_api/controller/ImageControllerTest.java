package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.entity.Image;
import com.enigma.wmb_api.service.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ImageControllerTest {
    @MockBean
    private ImageService imageService;
    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testDownload_shouldReturn200_whenDownloadImage() throws Exception {
        Image mockImage = Image.builder()
                .id("1")
                .name("image")
                .contentType("image/jpeg")
                .build();
        MultipartFile mockFile = new MockMultipartFile(mockImage.getName(), mockImage.getName()+".jpg", mockImage.getContentType(), "test data".getBytes());

        Resource mockResource = new ByteArrayResource(mockFile.getOriginalFilename().getBytes());

        when(imageService.getById(anyString())).thenReturn(mockResource);
        mockMvc.perform(
                        MockMvcRequestBuilders.get(RouteApi.MENU_IMAGE_API_PATH + mockImage.getId())
                                .contentType(mockImage.getContentType())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    assertEquals(200, result.getResponse().getStatus());
                });
    }
}