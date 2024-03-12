package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.repository.RoleRepository;
import com.enigma.wmb_api.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    void getOrSave_returnsExistingRole_whenRoleExists() {
        UserRole userRole = UserRole.ROLE_CUSTOMER;

        Role role = Role.builder().role(userRole).build();

        when(roleRepository.findByRole(userRole)).thenReturn(Optional.of(role));

        Role returnedRole = roleService.getOrSave(userRole);

        assertNotNull(returnedRole);
        assertEquals(userRole, returnedRole.getRole());
    }

    @Test
    void getOrSave_savesAndReturnsNewRole_whenRoleDoesNotExist() {
        UserRole userRole = UserRole.ROLE_CUSTOMER;

        Role role = Role.builder().role(userRole).build();

        when(roleRepository.findByRole(userRole)).thenReturn(Optional.empty());
        when(roleRepository.saveAndFlush(any(Role.class))).thenReturn(role);

        Role returnedRole = roleService.getOrSave(userRole);

        assertNotNull(returnedRole);
        assertEquals(userRole, returnedRole.getRole());
    }
}