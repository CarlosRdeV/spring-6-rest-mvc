package com.guru.spring_6_rest_mvc.controller;

import com.guru.spring_6_rest_mvc.model.CustomerDTO;
import com.guru.spring_6_rest_mvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class CustomerController {

    public static final String CUSTOMER_URI = "/api/v1/customer";
    public static final String CUSTOMER_BY_ID_URI = CUSTOMER_URI + "/{customerId}";
    private final CustomerService customerService;

    @PatchMapping(CUSTOMER_BY_ID_URI)
    public ResponseEntity patchCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {

        log.debug("CustomerController -> patchCustomerById -> customerId: {} - customer {}", customerId, customer);
        if (customerService.patchCustomerById(customerId, customer).isEmpty()) {
            throw new NotFoundException();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(CUSTOMER_BY_ID_URI)
    public ResponseEntity deleteById(@PathVariable("customerId") UUID customerId) {
        log.debug("CustomerController -> deleteById -> customerId: {}", customerId);
        if (!customerService.deleteById(customerId)) {
            throw new NotFoundException();
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping(CUSTOMER_BY_ID_URI)
    public ResponseEntity updateById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {
        log.debug("CustomerController -> updateById -> customerId: {} - customer {}", customerId, customer);
        if (customerService.updateById(customerId, customer).isEmpty()) {
            throw new NotFoundException();
        }
        // return new ResponseEntity(HttpStatus.NO_CONTENT); es lo mismo
        return ResponseEntity.noContent().build();
    }

    @PostMapping(CUSTOMER_URI)
    public ResponseEntity handlePost(@RequestBody CustomerDTO customer) {
        log.debug("CustomerController -> handlePost -> customer: {}", customer);
        CustomerDTO savedCustomer = customerService.saveNewCustomer(customer);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCustomer.getId())
                .toUri();
        return ResponseEntity.created(location).body(null);

    }

    @GetMapping(CUSTOMER_URI)
    public List<CustomerDTO> listCustomer() {
        log.debug("CustomerController -> listCustomer");
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_BY_ID_URI)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerId) {
        log.debug("CustomerController -> getCustomerById -> customerId: {}", customerId);
        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }
}
