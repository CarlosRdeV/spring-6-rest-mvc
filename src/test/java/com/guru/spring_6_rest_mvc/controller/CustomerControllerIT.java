package com.guru.spring_6_rest_mvc.controller;

import com.guru.spring_6_rest_mvc.entities.Customer;
import com.guru.spring_6_rest_mvc.mappers.CustomerMapper;
import com.guru.spring_6_rest_mvc.model.CustomerDTO;
import com.guru.spring_6_rest_mvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteById() {
        Customer customer = customerRepository.findAll().getFirst();
        ResponseEntity responseEntity = customerController.deleteById(customer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Optional<Customer> deletedCustomer = customerRepository.findById(customer.getId());
        assertThat(deletedCustomer).isEmpty();
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.updateById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void updateExistingCustomer() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String customerName = "New Customer";
        customerDTO.setCustomerName(customerName);

        ResponseEntity responseEntity = customerController.updateById(customer.getId(),customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updaterCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updaterCustomer.getCustomerName()).isEqualTo(customerName);
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("New Customer")
                .build();
        ResponseEntity responseEntity = customerController.handlePost(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[1]);

        Customer customer = customerRepository.findById(savedUUID).get();

        assertThat(customer).isNotNull();
    }

    @Test
    void testGetById() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDTO dto = customerController.getCustomerById(customer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Test
    void testCustomerList() {
        List<CustomerDTO> dtos = customerController.listCustomer();

        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyCustomerList() {
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.listCustomer();

        assertThat(dtos.size()).isEqualTo(0);
    }
}