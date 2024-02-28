package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.MenuRequest;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.MenuRepository;
import com.enigma.wmb_api.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    public Menu create(MenuRequest request) {
        Menu newMenu = Menu.builder()
                .menuName(request.getMenuName())
                .price(request.getPrice())
                .build();
        return menuRepository.saveAndFlush(newMenu);
    }

    @Override
    public Menu getById(String id) {
        return findBydIdThrowNotFound(id);    }

    @Override
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @Override
    public Menu update(Menu request) {

        findBydIdThrowNotFound(request.getId());
        return menuRepository.saveAndFlush(request);
    }

    @Override
    public void delete(String id) {
        Menu currentMenu = findBydIdThrowNotFound(id);
        menuRepository.delete(currentMenu);
    }

    @Override
    public Page<Menu> getAllWithPagination(Integer pageNumber, Integer pageSize, String sort) {
        Pageable pageable;
        if (sort != null) {
            pageable = PageRequest.of(pageNumber,pageSize, Sort.Direction.DESC,sort);
        } else {
            pageable = PageRequest.of(pageNumber,pageSize);
        }
        return menuRepository.findAll(pageable);
    }

    private Menu findBydIdThrowNotFound(String id) {
        return menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu Not Found"));
    }
}
