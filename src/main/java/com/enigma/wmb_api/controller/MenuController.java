package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.dto.request.MenuRequest;
import com.enigma.wmb_api.dto.request.PaginationMenuRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.MenuResponse;
import com.enigma.wmb_api.dto.response.PaginationResponse;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.service.MenuService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = RouteApi.MENU_PATH)
@Tag(name = "Menu", description = "Menu API")
public class MenuController {

    private final MenuService menuService;
    private final ObjectMapper objectMapper;

    @Operation(
            summary = "Create new menu",
            description = "Create new menu"
    )
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> create(
            @RequestPart(name = "menu") String jsonMenu,
            @RequestPart(name = "image", required = false) MultipartFile image
    ) {
        CommonResponse.CommonResponseBuilder<MenuResponse> builder = CommonResponse.builder();
        try {
            MenuRequest request = objectMapper.readValue(jsonMenu, new TypeReference<>() {
            });
            request.setImage(image);

            MenuResponse response = menuService.create(request);
            builder.statusCode(HttpStatus.CREATED.value());
            builder.message("Created Data Successfully");
            builder.data(response);
            return ResponseEntity.status(HttpStatus.CREATED).body(builder.build());
        } catch (Exception e) {
            builder.message(e.getMessage());
            builder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(builder.build());
        }
    }

    @Operation(
            summary = "Get all menu",
            description = "Get all menu"
    )
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','CUSTOMER')")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<Menu>>> getAll(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "menuName") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "menuName", required = false) String name,
            @RequestParam(name = "minPrice", required = false) Integer minPrice,
            @RequestParam(name = "maxPrice", required = false) Integer maxPrice
    ) {
        PaginationMenuRequest pageRequest = PaginationMenuRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .menuName(name)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .build();

        Page<Menu> result = menuService.getAll(pageRequest);

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalPages(result.getTotalPages())
                .totalElements(result.getTotalElements())
                .page(result.getPageable().getPageNumber() + 1)
                .size(result.getPageable().getPageSize())
                .hasNext(result.hasNext())
                .hasPrevious(result.hasPrevious())
                .build();

        CommonResponse<List<Menu>> responses = CommonResponse.<List<Menu>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all data successfully")
                .data(result.getContent())
                .pages(paginationResponse)
                .build();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(
            summary = "Get menu by id",
            description = "Get menu by id"
    )
    @SecurityRequirement(name = "Authorization")
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<Menu>> findById(@PathVariable String id) {
        Menu result = menuService.getById(id);
        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get data successfully")
                .data(result)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Update menu",
            description = "Update menu"
    )
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PutMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> update(
            @RequestPart(name = "menu") String jsonMenu,
            @RequestPart(name = "image", required = false) MultipartFile image
    ) {
        CommonResponse.CommonResponseBuilder<MenuResponse> builder = CommonResponse.builder();
        try {
            MenuRequest request = objectMapper.readValue(jsonMenu, new TypeReference<>() {
            });
            request.setImage(image);

            MenuResponse response = menuService.update(request);
            builder.statusCode(HttpStatus.CREATED.value());
            builder.message("Created Data Successfully");
            builder.data(response);
            return ResponseEntity.status(HttpStatus.CREATED).body(builder.build());
        } catch (Exception e) {
            builder.message(e.getMessage());
            builder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(builder.build());
        }
    }

    @Operation(
            summary = "Delete menu",
            description = "Delete menu"
    )
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @DeleteMapping(
            path = "/{id}"
    )
    public ResponseEntity<CommonResponse<Menu>> delete(@PathVariable String id) {
        menuService.delete(id);
        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Deleted data successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
