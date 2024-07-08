package com.guru.spring_6_rest_mvc.controller;

import com.guru.spring_6_rest_mvc.model.Beer;
import com.guru.spring_6_rest_mvc.model.Customer;
import com.guru.spring_6_rest_mvc.services.BeerService;
import com.guru.spring_6_rest_mvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> listCustomer() {
        log.debug("CustomerController -> listCustomer");
        return customerService.listCustomers();
    }

    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    public Customer getCustomerById( @PathVariable("customerId") UUID customerId  ){
        log.debug("CustomerController -> getCustomerById -> customerId: {}", customerId);
        return customerService.getCustomerById(customerId);
    }
}
