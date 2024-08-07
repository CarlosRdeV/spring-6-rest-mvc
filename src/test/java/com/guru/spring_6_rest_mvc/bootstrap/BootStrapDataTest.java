package com.guru.spring_6_rest_mvc.bootstrap;

import com.guru.spring_6_rest_mvc.repositories.BeerRepository;
import com.guru.spring_6_rest_mvc.repositories.CustomerRepository;
import com.guru.spring_6_rest_mvc.services.BeerCsvService;
import com.guru.spring_6_rest_mvc.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)
class BootStrapDataTest {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerCsvService beerCsvService;

    BootStrapData bootStrapData;

    @BeforeEach
    void setUp() {
        bootStrapData = new BootStrapData(beerRepository , customerRepository, beerCsvService);
    }

    @Test
    void TestRun() throws Exception{
        bootStrapData.run();

        assertThat(beerRepository.count()).isEqualTo(2413);
        assertThat(customerRepository.count()).isEqualTo(3);
    }
}