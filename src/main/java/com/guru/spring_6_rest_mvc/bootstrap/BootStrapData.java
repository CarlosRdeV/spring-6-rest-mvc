package com.guru.spring_6_rest_mvc.bootstrap;

import com.guru.spring_6_rest_mvc.entities.Beer;
import com.guru.spring_6_rest_mvc.entities.Customer;
import com.guru.spring_6_rest_mvc.model.BeerStyle;
import com.guru.spring_6_rest_mvc.repositories.BeerRepository;
import com.guru.spring_6_rest_mvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class BootStrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;

    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {

        if (beerRepository.count() == 0) {
            loadBeerData();
        }

        if (customerRepository.count() == 0) {
            loadCustomerData();
        }
    }

    private void loadBeerData() {
        log.debug("BootStrapData -> loadBeerData");
        Beer beer1 = Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356333")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerRepository.saveAll(Arrays.asList(beer1, beer2, beer3));

    }

    private void loadCustomerData() {
        log.debug("BootStrapData -> loadCustomerData");

        Customer customer1 = Customer.builder()
                .customerName("Nissaxter")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .customerName("Xavy")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .customerName("Magnazero")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerRepository.saveAll(Arrays.asList(customer1,customer2,customer3));

    }
}
