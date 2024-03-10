package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.MenuRequest;
import com.enigma.wmb_api.dto.request.PaginationMenuRequest;
import com.enigma.wmb_api.dto.response.MenuResponse;
import com.enigma.wmb_api.entity.Menu;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MenuService {
    MenuResponse create(MenuRequest request);
    MenuResponse getOneById(String id);
    Menu getById(String id);
    Page<Menu> getAll(PaginationMenuRequest request);
    List<Menu> getAll();
    MenuResponse update(MenuRequest request);
    void delete(String id);
}
