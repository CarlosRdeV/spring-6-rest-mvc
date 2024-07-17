package com.guru.spring_6_rest_mvc.controller;

import com.guru.spring_6_rest_mvc.model.Customer;
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
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PatchMapping(value = "{customerId}")
    public ResponseEntity patchCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
        customerService.patchCustomerById(customerId, customer);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "{customerId}")
    public ResponseEntity deleteById(@PathVariable("customerId") UUID customerId) {
        log.debug("CustomerController -> deleteById -> customerId: {}", customerId);
        customerService.deleteById(customerId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "{customerId}")
    public ResponseEntity updateById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
        log.debug("CustomerController -> updateById -> customerId: {} - customer {}", customerId, customer);
        customerService.updateById(customerId, customer);
        // return new ResponseEntity(HttpStatus.NO_CONTENT); es lo mismo
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity handlePost(@RequestBody Customer customer) {
        log.debug("CustomerController -> handlePost -> customer: {}", customer);
        Customer savedCustomer = customerService.saveNewCustomer(customer);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCustomer.getId())
                .toUri();
        return ResponseEntity.created(location).body(null);

    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> listCustomer() {
        log.debug("CustomerController -> listCustomer");
        return customerService.listCustomers();
    }

    @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId") UUID customerId) {
        log.debug("CustomerController -> getCustomerById -> customerId: {}", customerId);
        return customerService.getCustomerById(customerId);
    }
}
