package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.dto.request.UpdateCustomerRequest;
import com.enigma.wmb_api.dto.request.PaginationCustomerRequest;
import com.enigma.wmb_api.dto.response.AccountResponse;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.CustomerResponse;
import com.enigma.wmb_api.dto.response.PaginationResponse;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = RouteApi.CUSTOMER_PATH)
public class CustomerController {
    private final CustomerService customerService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAll(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "customerName") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "customerName", required = false) String name
    ) {
        PaginationCustomerRequest pageRequest = PaginationCustomerRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .name(name)
                .build();
        Page<Customer> result = customerService.getAll(pageRequest);

        List<CustomerResponse> customerResponses = result.getContent().stream()
                .map(this::mapToCustomerResponse)
                .collect(Collectors.toList());

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalPages(result.getTotalPages())
                .totalElements(result.getTotalElements())
                .page(result.getPageable().getPageNumber()+1)
                .size(result.getPageable().getPageSize())
                .hasNext(result.hasNext())
                .hasPrevious(result.hasPrevious())
                .build();

        CommonResponse<List<CustomerResponse>> responses = CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all data successfully")
                .data(customerResponses)
                .pages(paginationResponse)
                .build();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<CustomerResponse>> findById(@PathVariable String id) {

        Customer result = customerService.getById(id);

        CustomerResponse customerResponse = CustomerResponse.builder()
                .id(result.getId())
                .customerName(result.getCustomerName())
                .phoneNumber(result.getPhoneNumber())
                .accountDetails(mapToUserAccountResponse(result.getUserAccount()))
                .build();

        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get data successfully")
                .data(customerResponse)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN') or @authorizeSecurity.checkSameIdAsPrincipal(#request)")
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<CustomerResponse>> update(@RequestBody UpdateCustomerRequest request) {

        Customer result = customerService.update(request);

        CustomerResponse customerResponse = CustomerResponse.builder()
                .id(result.getId())
                .customerName(result.getCustomerName())
                .phoneNumber(result.getPhoneNumber())
                .accountDetails(mapToUserAccountResponse(result.getUserAccount()))
                .build();

        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Updated data successfully")
                .data(customerResponse)
                .build();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @DeleteMapping(
            path = "/{id}"
    )
    public ResponseEntity<CommonResponse<Customer>> delete(@PathVariable String id) {
        customerService.delete(id);
        CommonResponse<Customer> response = CommonResponse.<Customer>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Deleted data successfully")
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    private CustomerResponse mapToCustomerResponse(Customer customer) {
        CustomerResponse.CustomerResponseBuilder builder = CustomerResponse.builder()
                .id(customer.getId())
                .customerName(customer.getCustomerName())
                .phoneNumber(customer.getPhoneNumber());

        if (customer.getUserAccount() != null) {
            builder.accountDetails(mapToUserAccountResponse(customer.getUserAccount()));
        }

        return builder.build();
    }

    private AccountResponse mapToUserAccountResponse(UserAccount userAccount) {
        if (userAccount != null) {
            List<String> roles = userAccount.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).toList();
            return AccountResponse.builder()
                    .id(userAccount.getId())
                    .email(userAccount.getEmail())
                    .roles(roles)
                    .build();
        }
        return null;
    }

}
