package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.dto.request.AuthRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.LoginResponse;
import com.enigma.wmb_api.dto.response.RegisterResponse;
import com.enigma.wmb_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(RouteApi.AUTH_API)
public class AuthController {
    private final AuthService authService;

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> register(@RequestBody AuthRequest request) {
        RegisterResponse registerResponse = authService.register(request);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Created a new account successfully, please verify your email")
                .data(registerResponse)
                .build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> login(@RequestBody AuthRequest request) {
        LoginResponse loginResponse = authService.login(request);
        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Created a new account successfully")
                .data(loginResponse)
                .build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @GetMapping(
            path = "/verify-account"
    )
    public ResponseEntity<CommonResponse<?>> verifyAccount(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "otp") String otp
    ){
        String verifyAccount = authService.verifyAccount(email, otp);
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message(verifyAccount)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping(
            path = "/regenerate-otp"
    )
    public ResponseEntity<CommonResponse<?>> regenerateOtp(
            @RequestParam(name = "email") String email
    ){
        String verifyAccount = authService.regenerateOtp(email);
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message(verifyAccount)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
