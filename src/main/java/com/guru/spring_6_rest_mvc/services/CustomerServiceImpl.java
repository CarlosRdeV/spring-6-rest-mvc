package com.guru.spring_6_rest_mvc.services;

import com.guru.spring_6_rest_mvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        log.debug("CustomerServiceImpl -> constructor");
        this.customerMap = new HashMap<>();

        CustomerDTO customer1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Nissaxter")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Xavy")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customer3 = CustomerDTO.builder()
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
    public List<CustomerDTO> listCustomers() {
        log.debug("CustomerServiceImpl -> listCustomers");
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        log.debug("CustomerServiceImpl -> getCustomerById -> customerId: {}", customerId);
        return Optional.of(customerMap.get(customerId));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        log.debug("CustomerServiceImpl -> saveNewCustomer -> customer: {}", customer);
        CustomerDTO saverCustomer = CustomerDTO.builder()
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
    public void updateById(UUID customerId, CustomerDTO customer) {
        log.debug("CustomerServiceImpl -> updateById -> customerId: {} - customer {}", customerId, customer);
        CustomerDTO existing = customerMap.get(customerId);
        existing.setVersion(existing.getVersion()+1);
        existing.setCustomerName(customer.getCustomerName());
        existing.setCreatedDate(existing.getCreatedDate());
        existing.setUpdateDate(LocalDateTime.now());

        customerMap.put(existing.getId(), existing);
    }

    @Override
    public void deleteById(UUID customerId) {
        log.debug("CustomerServiceImpl -> deleteById -> customerId: {}", customerId);
        customerMap.remove(customerId);
    }

    @Override
    public void patchCustomerById(UUID customerId, CustomerDTO customer) {
        log.debug("CustomerServiceImpl -> updateCustomerPatchById -> customerId: {} - customer {}", customerId, customer);
        CustomerDTO existing = customerMap.get(customerId);

        if(StringUtils.hasText(customer.getCustomerName())) {
            existing.setCustomerName(customer.getCustomerName());
        }
    }
}
