package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.MenuRequest;
import com.enigma.wmb_api.dto.request.PaginationMenuRequest;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.MenuRepository;
import com.enigma.wmb_api.service.MenuService;
import com.enigma.wmb_api.specification.MenuSpecification;
import com.enigma.wmb_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final ValidationUtil validation;

    @Override
    public Menu create(MenuRequest request) {
        validation.validate(request);
        Menu newMenu = Menu.builder()
                .menuName(request.getMenuName())
                .price(request.getPrice())
                .build();
        return menuRepository.saveAndFlush(newMenu);
    }

    @Override
    public Menu getById(String id) {
        return findBydIdThrowNotFound(id);
    }

    @Override
    public Page<Menu> getAll(PaginationMenuRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);
        Specification<Menu> specification = MenuSpecification.getSpecification(request);

        return menuRepository.findAll(specification, pageable);
    }

    @Override
    public Menu update(Menu request) {
        findBydIdThrowNotFound(request.getId());
        validation.validate(request);

        return menuRepository.saveAndFlush(request);
    }

    @Override
    public void delete(String id) {
        Menu currentMenu = findBydIdThrowNotFound(id);
        menuRepository.delete(currentMenu);
    }


    private Menu findBydIdThrowNotFound(String id) {
        return menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu Not Found"));
    }
}
