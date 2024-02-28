package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.MenuRequest;
import com.enigma.wmb_api.entity.Menu;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MenuService {
    Menu create(MenuRequest request);
    Menu getById(String id);
    List<Menu> getAll();
    Menu update(Menu request);
    void delete(String id);
    Page<Menu> getAllWithPagination(Integer pageNumber, Integer pageSize, String sort);
}
