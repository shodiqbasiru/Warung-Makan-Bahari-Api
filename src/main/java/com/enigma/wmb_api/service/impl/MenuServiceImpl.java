package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.dto.request.MenuRequest;
import com.enigma.wmb_api.dto.request.PaginationMenuRequest;
import com.enigma.wmb_api.dto.response.ImageResponse;
import com.enigma.wmb_api.dto.response.MenuResponse;
import com.enigma.wmb_api.entity.Image;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.MenuRepository;
import com.enigma.wmb_api.service.ImageService;
import com.enigma.wmb_api.service.MenuService;
import com.enigma.wmb_api.specification.MenuSpecification;
import com.enigma.wmb_api.util.ValidationUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final ValidationUtil validation;
    private final ImageService imageService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse create(MenuRequest request) {
        validation.validate(request);
        if (request.getImage().isEmpty()) throw new ConstraintViolationException("image is required", null);
        Image image = imageService.create(request.getImage());
        Menu newMenu = Menu.builder()
                .menuName(request.getMenuName())
                .price(request.getPrice())
                .image(image)
                .build();
        menuRepository.saveAndFlush(newMenu);
        return convertMenuToMenuResponse(newMenu);
    }

    @Transactional(readOnly = true)
    @Override
    public MenuResponse getOneById(String id) {
        Menu menu = findBydIdThrowNotFound(id);
        return convertMenuToMenuResponse(menu);
    }


    @Transactional(readOnly = true)
    @Override
    public Menu getById(String id) {
        return findBydIdThrowNotFound(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Menu> getAll(PaginationMenuRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);
        Specification<Menu> specification = MenuSpecification.getSpecification(request);

        return menuRepository.findAll(specification, pageable);
    }

    @Override
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse update(MenuRequest request) {
        validation.validate(request);
        Menu currentMenu = findBydIdThrowNotFound(request.getId());
        Image oldImage = currentMenu.getImage();
        currentMenu.setMenuName(request.getMenuName());
        currentMenu.setPrice(request.getPrice());

        if (request.getImage() != null) {
            Image image = imageService.create(request.getImage());
            currentMenu.setImage(image);

            if (oldImage != null) {
                imageService.deleteById(oldImage.getId());
            }
        }
        currentMenu = menuRepository.saveAndFlush(currentMenu);

        return convertMenuToMenuResponse(currentMenu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Menu currentMenu = findBydIdThrowNotFound(id);
        menuRepository.delete(currentMenu);
    }

    private Menu findBydIdThrowNotFound(String id) {
        return menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu Not Found"));
    }

    private MenuResponse convertMenuToMenuResponse(Menu newMenu) {
        return MenuResponse.builder()
                .id(newMenu.getId())
                .name(newMenu.getMenuName())
                .price(newMenu.getPrice())
                .image(ImageResponse.builder()
                        .url(RouteApi.MENU_IMAGE_API_PATH + newMenu.getImage().getId())
                        .name(newMenu.getImage().getName())
                        .build())
                .build();
    }

}
