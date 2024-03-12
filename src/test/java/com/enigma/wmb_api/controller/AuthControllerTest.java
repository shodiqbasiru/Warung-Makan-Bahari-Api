package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.dto.request.AuthRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.LoginResponse;
import com.enigma.wmb_api.dto.response.RegisterResponse;
import com.enigma.wmb_api.service.AuthService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @MockBean
    private AuthService authService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister_shouldReturn201_whenRegisterCustomer() throws Exception {
        AuthRequest payload = AuthRequest.builder()
                .name("fulan")
                .email("fulan@gmail.com")
                .password("12345")
                .build();
        RegisterResponse mockResponse = RegisterResponse.builder()
                .email(payload.getEmail())
                .roles(List.of("CUSTOMER"))
                .build();

        when(authService.register(payload)).thenReturn(mockResponse);
        String stringJson = objectMapper.writeValueAsString(payload);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(RouteApi.AUTH_API + "/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stringJson)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<RegisterResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertNotNull(response);
                    assertEquals(201, response.getStatusCode());
                    assertEquals("Created a new account successfully, please verify your email", response.getMessage());
                });
    }

    @Test
    void testRegisterAdmin_shouldReturn201_whenRegisterAdmin() throws Exception {
        AuthRequest payload = AuthRequest.builder()
                .name("admin")
                .email("admin@gmail.com")
                .password("12345")
                .build();
        RegisterResponse mockResponse = RegisterResponse.builder()
                .email(payload.getEmail())
                .roles(List.of("ROLE_ADMIN"))
                .build();

        when(authService.register(payload)).thenReturn(mockResponse);
        String stringJson = objectMapper.writeValueAsString(payload);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(RouteApi.AUTH_API + "/register/admin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stringJson)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<RegisterResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertNotNull(response);
                    assertEquals(201, response.getStatusCode());
                    assertEquals("Created a new account successfully, please verify your email", response.getMessage());
                });
    }

    @Test
    void testLogin_shouldReturn200_whenLogin() throws Exception {
        AuthRequest payload = AuthRequest.builder()
                .name("fulan")
                .email("fulan@gmail.com")
                .password("12345")
                .build();
        LoginResponse mockResponse = LoginResponse.builder()
                .email(payload.getEmail())
                .roles(List.of("CUSTOMER"))
                .build();

        when(authService.login(payload)).thenReturn(mockResponse);

        String stringJson = objectMapper.writeValueAsString(payload);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(RouteApi.AUTH_API + "/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stringJson)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<LoginResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertNotNull(response);
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Created a new account successfully", response.getMessage());
                });
    }

    @Test
    void testVerifyAccount_shouldReturn200_whenVerifyAccount() throws Exception {
        String email = "fulan@gmail.com";
        String otp = "212305";

        when(authService.verifyAccount(email, otp)).thenReturn(email, otp);
        mockMvc.perform(
                        MockMvcRequestBuilders.get(RouteApi.AUTH_API + "/verify-account")
                                .param("email", email)
                                .param("otp", otp)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<?> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertNotNull(response);
                    assertEquals(200, response.getStatusCode());
                    assertEquals("verify account successfully", response.getMessage());
                });
    }

    @Test
    void regenerateOtp() throws Exception {
        String email = "fulan@gmail.com";
        when(authService.regenerateOtp(email)).thenReturn(email);
        mockMvc.perform(
                        MockMvcRequestBuilders.get(RouteApi.AUTH_API + "/regenerate-otp")
                                .param("email", email)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<?> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertNotNull(response);
                    assertEquals(200, response.getStatusCode());
                    assertEquals("regenerate otp successfully", response.getMessage());
                });
    }
}