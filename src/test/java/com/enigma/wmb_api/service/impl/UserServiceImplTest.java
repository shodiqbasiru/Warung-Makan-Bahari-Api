package com.enigma.wmb_api.service.impl;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserAccountRepository accountRepository;

    @Mock
    private Authentication authentication;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(accountRepository);
    }

    @Test
    void shouldReturnUserWhenUserIdExists() {
        String userId = "123";
        UserAccount userAccount = new UserAccount();
        when(accountRepository.findById(userId)).thenReturn(Optional.of(userAccount));

        UserAccount result = userService.getByUserId(userId);

        assertEquals(userAccount, result);
    }

    @Test
    void shouldThrowExceptionWhenUserIdDoesNotExist() {
        String userId = "123";
        when(accountRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.getByUserId(userId));
    }

    @Test
    void shouldReturnUserWhenContextUserExists() {
        String email = "test@test.com";
        UserAccount userAccount = new UserAccount();
        when(authentication.getPrincipal()).thenReturn(email);
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(userAccount));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserAccount result = userService.getByContext();

        assertEquals(userAccount, result);
    }

    @Test
    void shouldThrowExceptionWhenContextUserDoesNotExist() {
        String email = "test@test.com";
        when(authentication.getPrincipal()).thenReturn(email);
        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThrows(ResponseStatusException.class, () -> userService.getByContext());
    }

    @Test
    void shouldReturnUserDetailsWhenUsernameExists() {
        String username = "test";
        UserAccount userAccount = new UserAccount();
        when(accountRepository.findByEmail(username)).thenReturn(Optional.of(userAccount));

        UserDetails result = userService.loadUserByUsername(username);

        assertEquals(userAccount, result);
    }

    @Test
    void shouldThrowExceptionWhenUsernameDoesNotExist() {
        String username = "test";
        when(accountRepository.findByEmail(username)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.loadUserByUsername(username));
    }
}