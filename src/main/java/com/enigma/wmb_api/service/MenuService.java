package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.MenuRequest;
import com.enigma.wmb_api.dto.request.PaginationCustomerRequest;
import com.enigma.wmb_api.dto.request.PaginationMenuRequest;
import com.enigma.wmb_api.entity.Menu;
import org.springframework.data.domain.Page;

public interface MenuService {
    Menu create(MenuRequest request);
    Menu getById(String id);
    Page<Menu> getAll(PaginationMenuRequest request);
    Menu update(Menu request);
    void delete(String id);
}
