package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.dto.request.AuthRequest;
import com.enigma.wmb_api.dto.request.EmailRequest;
import com.enigma.wmb_api.dto.response.LoginResponse;
import com.enigma.wmb_api.dto.response.RegisterResponse;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.repository.UserAccountRepository;
import com.enigma.wmb_api.service.*;
import com.enigma.wmb_api.util.ValidationUtil;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final JwtService jwtService;
    private final ValidationUtil validation;
    private final EmailService emailService;
    private final OtpGenerationService otpService;
    private final AuthenticationManager authenticationManager;

    @Value("${enigma_shop.email.superadmin}")
    private String superAdminEmail;
    @Value("${enigma_shop.username.password}")
    private String superAdminPassword;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initSuperAdmin() {
        Optional<UserAccount> currentUser = userRepository.findByEmail(superAdminEmail);
        if (currentUser.isPresent()) return;

        Role superAdmin = roleService.getOrSave(UserRole.ROLE_SUPER_ADMIN);
        Role admin = roleService.getOrSave(UserRole.ROLE_ADMIN);
        Role customer = roleService.getOrSave(UserRole.ROLE_CUSTOMER);

        UserAccount account = UserAccount.builder()
                .email("superadmin@gmail.com")
                .password(passwordEncoder.encode(superAdminPassword))
                .roles(List.of(superAdmin,admin,customer))
                .isEnable(true)
                .isVerified(true)
                .build();
        userRepository.save(account);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse register(AuthRequest request) {
        validation.validate(request);

        Role role = roleService.getOrSave(UserRole.ROLE_CUSTOMER);
        String hashPassword = passwordEncoder.encode(request.getPassword());

        String otp = otpService.generateOtp();

        UserAccount account = UserAccount.builder()
                .email(request.getEmail())
                .password(hashPassword)
                .roles(List.of(role))
                .otp(otp)
                .otpGenerateTime(LocalDateTime.now())
                .isVerified(false)
                .isEnable(true)
                .build();
        userRepository.saveAndFlush(account);

        Customer customer = Customer.builder()
                .customerName(request.getName())
                .userAccount(account)
                .build();
        customerService.create(customer);

        EmailRequest emailRequest = EmailRequest.builder()
                .to(request.getEmail())
                .subject("Email Verification OTP")
                .body("""
                        <h3>Click the link below to verify your account</h3>
                        <div>
                            <a href="http://localhost:8082/api/auth/verify-account?email=%s&otp=%s" target="_blank"> Click the link to verify</a>
                        </div>
                        """.formatted(request.getEmail(), otp))
                .build();
        try {
            emailService.sendEmail(emailRequest);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send email");
        }

        List<String> roles = account.getAuthorities().stream()
                .map((GrantedAuthority::getAuthority)).toList();
        return RegisterResponse.builder().email(account.getUsername()).roles(roles).build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerAdmin(AuthRequest request) {
        Role roleAdmin = roleService.getOrSave(UserRole.ROLE_ADMIN);
        Role roleCustomer = roleService.getOrSave(UserRole.ROLE_CUSTOMER);

        String hashPassword = passwordEncoder.encode(request.getPassword());
        String otp = otpService.generateOtp();

        UserAccount account = UserAccount.builder()
                .email(request.getEmail())
                .password(hashPassword)
                .roles(List.of(roleAdmin,roleCustomer))
                .otp(otp)
                .otpGenerateTime(LocalDateTime.now())
                .isEnable(true)
                .isVerified(false)
                .build();

        userRepository.saveAndFlush(account);

        Customer customer = Customer.builder()
                .customerName(request.getName())
                .userAccount(account)
                .build();
        customerService.create(customer);

        EmailRequest emailRequest = EmailRequest.builder()
                .to(request.getEmail())
                .subject("Email Verification OTP")
                .body("""
                        <h3>Click the link below to verify your account</h3>
                        <div>
                            <a href="http://localhost:8082/api/auth/verify-account?email=%s&otp=%s" target="_blank"> Click the link to verify</a>
                        </div>
                        """.formatted(request.getEmail(), otp))
                .build();
        try {
            emailService.sendEmail(emailRequest);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send email");
        }

        List<String> roles = account.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return RegisterResponse.builder()
                .email(account.getUsername())
                .roles(roles)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(AuthRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserAccount account = (UserAccount) authenticate.getPrincipal();
        if (!account.getIsVerified()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Your account is not verified");
        }
        String token = jwtService.generateToken(account);
        return LoginResponse.builder()
                .email(account.getEmail())
                .roles(account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .token(token)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String verifyAccount(String email, String otp) {
        UserAccount account = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with this email"));
        if (account.getOtp().equals(otp) && Duration.between(account.getOtpGenerateTime(), LocalDateTime.now()).getSeconds() < 60) {
            account.setIsVerified(true);
            userRepository.saveAndFlush(account);
            return "Email is verified, you can login now";
        }
        return "Please regenerate otp and try again";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String regenerateOtp(String email) {
        UserAccount account = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with this email"));
        String otp = otpService.generateOtp();

        EmailRequest emailRequest = EmailRequest.builder()
                .to(email)
                .subject("Email Verification OTP")
                .body("""
                        <h3>Click the link below to verify your account</h3>
                        <div>
                            <a href="http://localhost:8082/api/auth/verify-account?email=%s&otp=%s" target="_blank"> Click the link to verify</a>
                        </div>
                        """.formatted(email, otp))
                .build();
        try {
            emailService.sendEmail(emailRequest);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send email");
        }
        account.setOtp(otp);
        account.setOtpGenerateTime(LocalDateTime.now());
        userRepository.saveAndFlush(account);
        return "Email sent, please verify account within 1 minute";
    }
}
