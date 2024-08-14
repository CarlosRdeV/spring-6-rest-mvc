package com.guru.spring_6_rest_mvc.repositories;

import com.guru.spring_6_rest_mvc.bootstrap.BootStrapData;
import com.guru.spring_6_rest_mvc.entities.Beer;
import com.guru.spring_6_rest_mvc.model.BeerStyle;
import com.guru.spring_6_rest_mvc.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({BootStrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testFindAllByBeerNameIsLikeIgnoreCase() {
        Page<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%", null);
        assertThat(list).isNotEmpty();
        assertThat(list.getContent().size()).isEqualTo(336);

    }

    @Test
    void testSaveBeerTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            Beer savedBeer = beerRepository.save(
                    Beer.builder()
                            .beerName("New Beer01239401392131032Beer01239401392131032Beer01239401392131032Beer01239401392131032Beer01239401392131032Beer01239401392131032Beer01239401392131032")
                            .beerStyle(BeerStyle.IPA)
                            .upc("222222")
                            .price(new BigDecimal("99.99"))
                            .build()
            );
            beerRepository.flush();
        });

    }

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(
                Beer.builder()
                        .beerName("New Beer")
                        .beerStyle(BeerStyle.IPA)
                        .upc("222222")
                        .price(new BigDecimal("99.99"))
                        .build()
        );

        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

}