package com.guru.spring_6_rest_mvc.mappers;

import com.guru.spring_6_rest_mvc.entities.Beer;
import com.guru.spring_6_rest_mvc.entities.Customer;
import com.guru.spring_6_rest_mvc.model.BeerDTO;
import com.guru.spring_6_rest_mvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDTOToCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDTO(Customer customer);
}
