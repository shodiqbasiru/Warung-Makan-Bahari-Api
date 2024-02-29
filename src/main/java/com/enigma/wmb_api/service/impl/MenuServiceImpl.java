package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.MenuRequest;
import com.enigma.wmb_api.dto.request.PaginationRequest;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.exception.ApiException;
import com.enigma.wmb_api.repository.MenuRepository;
import com.enigma.wmb_api.service.MenuService;
import com.enigma.wmb_api.specification.CustomerSpecification;
import com.enigma.wmb_api.specification.MenuSpecifcation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public Page<Menu> getAll(PaginationRequest request) {
        if (request.getPage() < 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);
        Specification<Menu> specification = MenuSpecifcation.getSpecifcation(request);

        return menuRepository.findAll(specification,pageable);
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



    private Menu findBydIdThrowNotFound(String id) {
        return menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Menu Not Found"));
    }
}
