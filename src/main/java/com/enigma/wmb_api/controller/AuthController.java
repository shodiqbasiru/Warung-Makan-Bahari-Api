package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.dto.request.AuthRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.LoginResponse;
import com.enigma.wmb_api.dto.response.RegisterResponse;
import com.enigma.wmb_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(RouteApi.AUTH_API)
@Tag(name = "Auth", description = "Auth API")
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Register new user",
            description = "Register new user"
    )
    @SecurityRequirement(name = "Authorization")
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
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Register new admin",
            description = "Register new admin"
    )
    @SecurityRequirement(name = "Authorization")
    @PostMapping(
            path = "/register/admin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> registerAdmin(@RequestBody AuthRequest request) {
        RegisterResponse registerResponse = authService.registerAdmin(request);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Created a new account successfully, please verify your email")
                .data(registerResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Login",
            description = "Login"
    )
    @SecurityRequirement(name = "Authorization")
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
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Verify account",
            description = "Verify account"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping(
            path = "/verify-account"
    )
    public ResponseEntity<CommonResponse<?>> verifyAccount(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "otp") String otp
    ) {
        String verifyAccount = authService.verifyAccount(email, otp);
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message(verifyAccount)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Regenerate OTP",
            description = "Regenerate OTP"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping(
            path = "/regenerate-otp"
    )
    public ResponseEntity<CommonResponse<?>> regenerateOtp(
            @RequestParam(name = "email") String email
    ) {
        String verifyAccount = authService.regenerateOtp(email);
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message(verifyAccount)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
