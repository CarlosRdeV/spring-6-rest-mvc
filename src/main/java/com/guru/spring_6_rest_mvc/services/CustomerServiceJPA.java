package com.guru.spring_6_rest_mvc.services;

import com.guru.spring_6_rest_mvc.mappers.CustomerMapper;
import com.guru.spring_6_rest_mvc.model.CustomerDTO;
import com.guru.spring_6_rest_mvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> listCustomers() {
        log.debug("CustomerServiceJPA -> listCustomers");
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        log.debug("CustomerServiceJPA -> listCustomers");
        return Optional.ofNullable(
                customerMapper.customerToCustomerDTO(
                        customerRepository.findById(customerId)
                                .orElse(null)));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        log.debug("CustomerServiceJPA -> saveNewCustomer -> customer: {}", customer);
        return customerMapper.customerToCustomerDTO(
                customerRepository.save(
                        customerMapper.customerDTOToCustomer(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateById(UUID customerId, CustomerDTO customer) {
        log.debug("CustomerServiceJPA -> updateById -> customerId: {} -> customer: {}", customerId, customer);
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(customerId).ifPresentOrElse(
                foundCustomer -> {
                    foundCustomer.setCustomerName(customer.getCustomerName());
                    atomicReference.set(
                            Optional.of(
                                    customerMapper.customerToCustomerDTO(customerRepository.save(foundCustomer))));
                }, () -> {
                    atomicReference.set(Optional.empty());
                });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID customerId) {
        log.debug("CustomerServiceJPA -> deleteById -> customerId: {}", customerId);
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }

    @Override
    public void patchCustomerById(UUID customerId, CustomerDTO customer) {

    }
}
