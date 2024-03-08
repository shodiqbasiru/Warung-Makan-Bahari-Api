package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.dto.request.MenuRequest;
import com.enigma.wmb_api.dto.request.PaginationMenuRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.PaginationResponse;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = RouteApi.MENU_PATH)
public class MenuController {

    private final MenuService menuService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<Menu>> create(@RequestBody MenuRequest request) {
        Menu result = menuService.create(request);
        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Created a new menu successfully")
                .data(result)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<Menu>>> getAll(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "menuName") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "customerName", required = false) String name
    ) {
        PaginationMenuRequest pageRequest = PaginationMenuRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .name(name)
                .build();

        Page<Menu> result = menuService.getAll(pageRequest);

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalPages(result.getTotalPages())
                .totalElements(result.getTotalElements())
                .page(result.getPageable().getPageNumber()+1)
                .size(result.getPageable().getPageSize())
                .hasNext(result.hasNext())
                .hasPrevious(result.hasPrevious())
                .build();

        CommonResponse<List<Menu>> responses = CommonResponse.<List<Menu>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all menu successfully")
                .data(result.getContent())
                .pages(paginationResponse)
                .build();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<Menu>> findById(@PathVariable String id) {
        Menu result = menuService.getById(id);
        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get menu successfully")
                .data(result)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<Menu>> update(@RequestBody Menu request) {
        Menu result = menuService.update(request);
        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Updated menu successfully")
                .data(result)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @DeleteMapping(
            path = "/{id}"
    )
    public ResponseEntity<CommonResponse<Menu>> delete(@PathVariable String id) {
        menuService.delete(id);
        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Deleted menu successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
