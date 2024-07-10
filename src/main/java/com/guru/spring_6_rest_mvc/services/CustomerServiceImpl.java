package com.guru.spring_6_rest_mvc.services;

import com.guru.spring_6_rest_mvc.model.Beer;
import com.guru.spring_6_rest_mvc.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        log.debug("CustomerServiceImpl -> constructor");
        this.customerMap = new HashMap<>();

        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Nissaxter")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Xavy")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Magnazero")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);

    }
    @Override
    public List<Customer> listCustomers() {
        log.debug("CustomerServiceImpl -> listCustomers");
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Customer getCustomerById(UUID customerId) {
        log.debug("CustomerServiceImpl -> getCustomerById -> customerId: {}", customerId);
        return customerMap.get(customerId);
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {
        log.debug("CustomerServiceImpl -> saveNewCustomer -> customer: {}", customer);
        Customer saverCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .version(customer.getVersion())
                .customerName(customer.getCustomerName())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        customerMap.put(saverCustomer.getId(), saverCustomer);

        return saverCustomer;
    }

    @Override
    public void updateById(UUID customerId, Customer customer) {
        log.debug("CustomerServiceImpl -> updateById -> customerId: {} - customer {}", customerId, customer);
        Customer existing = customerMap.get(customerId);
        existing.setVersion(existing.getVersion()+1);
        existing.setCustomerName(customer.getCustomerName());
        existing.setCreatedDate(existing.getCreatedDate());
        existing.setUpdateDate(LocalDateTime.now());

        customerMap.put(existing.getId(), existing);
    }
}
