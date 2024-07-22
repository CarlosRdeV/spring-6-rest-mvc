package com.guru.spring_6_rest_mvc.repositories;

import com.guru.spring_6_rest_mvc.entities.Beer;
import com.guru.spring_6_rest_mvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {
        Customer customerSaved = customerRepository.save(
                Customer.builder()
                        .customerName("Customer Name")
                        .build()
        );

        assertThat(customerSaved).isNotNull();
        assertThat(customerSaved.getId()).isNotNull();
    }

}